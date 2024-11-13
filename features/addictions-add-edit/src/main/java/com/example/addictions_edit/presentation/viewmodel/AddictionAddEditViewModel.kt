package com.example.addictions_edit.presentation.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.addictions_edit.AchievementNotificationWorker
import com.example.addictions_edit.models.AddictionUI
import com.example.addictions_edit.usecase.GetAddictionByIdUseCase
import com.example.addictions_edit.usecase.GetAddictionByTypeUseCase
import com.example.addictions_edit.usecase.SavaAddictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.core_utils.MilestonesProvider
import dev.misufoil.core_utils.ResourceManager
import dev.misufoil.core_utils.date_time_utils.convertDateToString
import dev.misufoil.core_utils.date_time_utils.convertLongToStringDate
import dev.misufoil.core_utils.date_time_utils.formatTime
import dev.misufoil.core_utils.date_time_utils.getCurrentTimeString
import dev.misufoil.core_utils.date_time_utils.stringDateTimeToLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import dev.misufoil.addictions.uikit.R as uikitR

@HiltViewModel
internal class AddictionAddEditViewModel @Inject constructor(
    private val getAddictionByIdUseCase: Provider<GetAddictionByIdUseCase>,
    private val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    private val savaAddictionUseCase: Provider<SavaAddictionUseCase>,
    private val resourceManager: ResourceManager,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State = State.Loading()
        private set

    private val _uiState: MutableStateFlow<AddictionUI> = MutableStateFlow(AddictionUI.empty)
    val uiState: StateFlow<AddictionUI> = _uiState.asStateFlow()

    private val _showBottomSheet: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _toastMessageState: MutableStateFlow<String?> = MutableStateFlow(null)
    val toastMessageState: StateFlow<String?> = _toastMessageState.asStateFlow()

    private val _timeDialogState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val timeDialogState: StateFlow<Boolean> = _timeDialogState.asStateFlow()

    private val _dateDialogState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dateDialogState: StateFlow<Boolean> = _dateDialogState.asStateFlow()

    init {
        val savedParam = savedStateHandle.get<String>("addictionId")
        if (savedParam == "add") {
            val startUi = AddictionUI(
                id = "",
                type = resourceManager.getStringById(uikitR.string.alcohol),
                date = convertDateToString(LocalDate.now()),
                time = getCurrentTimeString(),
                daysPerWeek = 1,
                timesInDay = 1,
            )

            _uiState.update { startUi }
            state = State.Success(startUi)

        } else if (savedParam != null) {
            viewModelScope.launch {
                state = State.Loading()
                val result = getAddictionByIdUseCase.get().invoke(savedParam)

                state = result.toState()

                if (result is RequestResult.Success) {
                    initUi(result.data)
                } else {
                    println("Error fetching data: $result")
                }
            }
        } else {
            state = State.Error()
        }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            saveAddiction()
        }
    }

    fun updateBottomSheetState(show: Boolean) {
        _showBottomSheet.update { show }
    }

    fun updateTimeDialogState(show: Boolean) {
        _timeDialogState.update { show }
    }

    fun updateDateDialogState(show: Boolean) {
        _dateDialogState.update { show }
    }

    fun onTypeChange(type: String) {
        _uiState.update {
            it.copy(type = type)
        }
    }

    fun onDateChange(date: Long) {
        _uiState.update {
            it.copy(date = convertLongToStringDate(date))
        }
    }

    fun onTimeChange(time: Pair<Int, Int>) {
        _uiState.update {
            it.copy(time = formatTime(time.first, time.second))
        }
    }

    fun onDaysPerWeekChange(daysPerWeek: Int) {
        _uiState.update {
            it.copy(daysPerWeek = daysPerWeek)
        }
    }

    fun onTimesInDayChange(timesInDay: Int) {
        _uiState.update {
            it.copy(timesInDay = timesInDay)
        }
    }

    fun onToastMessageStateChange(toastText: String?) {
        _toastMessageState.update { toastText }
    }

    private suspend fun initUi(addiction: AddictionUI) {
        _uiState.emit(addiction)
    }

    private suspend fun saveAddiction() {
        if (uiState.value.id.isEmpty()) {
            _uiState.update {
                it.copy(id = UUID.randomUUID().toString())
            }
        }

        val existingAddiction = getAddictionByTypeUseCase.get().invoke(uiState.value.type)

        if (existingAddiction is RequestResult.Success && existingAddiction.data.id != uiState.value.id) {
            onToastMessageStateChange(resourceManager.getStringById(uikitR.string.addiction_exists_error))
            return
        }

        createMilestoneChain()
        // при редактировании пересоздаётся воркер и заменяет старый
        savaAddictionUseCase.get().invoke(uiState.value)
    }

    private fun createMilestoneChain() {
        val milestones = MilestonesProvider.milestones
        val pair = getNextMilestone()
        val firstMilestone = pair.first ?: return
        val timePassed = pair.second ?: return

        val firstMilestoneWorker = OneTimeWorkRequestBuilder<AchievementNotificationWorker>()
            .setInitialDelay(timePassed.toMillis(), TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "id" to uiState.value.id,
                    "title" to uiState.value.type,
                    "duration" to firstMilestone.toDays().toString()
                )
            )
            .build()


        var workContinuation = workManager.beginUniqueWork(
            uiState.value.type,
            ExistingWorkPolicy.REPLACE,
            firstMilestoneWorker
        )

        var previousMilestone = firstMilestone

        milestones.filter { it > firstMilestone }.forEach { milestone ->
            val delay = milestone.minus(previousMilestone).toMillis()
            previousMilestone = milestone

            val milestoneWorker = OneTimeWorkRequestBuilder<AchievementNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "id" to uiState.value.id,
                        "title" to uiState.value.type,
                        "duration" to milestone.toDays().toString()
                    )
                )
                .build()
            workContinuation = workContinuation.then(milestoneWorker)
        }
        workContinuation.enqueue()
    }


    private fun getNextMilestone(): Pair<Duration?, Duration?> {
        val currentDateTime = LocalDateTime.now()
        val initialValue = stringDateTimeToLocalDateTime(uiState.value.date, uiState.value.time)

        // Calculate time passed since initialValue
        val timePassed = Duration.between(initialValue, currentDateTime)

        // Find the closest milestone
        val milestoneIndex =
            MilestonesProvider.milestones.indexOfLast { timePassed >= it }.coerceAtLeast(0)
        val nextMilestone = MilestonesProvider.milestones.getOrNull(milestoneIndex + 1)

        val initialDelay = nextMilestone?.minus(timePassed)
        return Pair(nextMilestone, initialDelay)
    }
}

internal sealed class State(open val addiction: AddictionUI?) {
    data object None : State(addiction = null)
    class Loading(addiction: AddictionUI? = null) : State(addiction)
    class Error(addiction: AddictionUI? = null) : State(addiction)
    class Success(override val addiction: AddictionUI) : State(addiction)
}

private fun RequestResult<AddictionUI>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(data)
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}