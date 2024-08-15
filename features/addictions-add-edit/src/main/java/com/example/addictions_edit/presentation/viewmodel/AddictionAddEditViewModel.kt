package com.example.addictions_edit.presentation.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictions_edit.models.AddictionUI
import com.example.addictions_edit.usecase.GetAddictionByIdUseCase
import com.example.addictions_edit.usecase.GetAddictionByTypeUseCase
import com.example.addictions_edit.usecase.SavaAddictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.core_utils.ResourceManager
import dev.misufoil.core_utils.date_time_utils.convertDateToString
import dev.misufoil.core_utils.date_time_utils.convertLongToStringDate
import dev.misufoil.core_utils.date_time_utils.formatTime
import dev.misufoil.core_utils.date_time_utils.getCurrentTimeString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider
import dev.misufoil.addictions.uikit.R as uikitR

@HiltViewModel
internal class AddictionAddEditViewModel @Inject constructor(
    private val getAddictionByIdUseCase: Provider<GetAddictionByIdUseCase>,
    private val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    private val savaAddictionUseCase: Provider<SavaAddictionUseCase>,
    private val resourceManager: ResourceManager,
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
        val savedParam = savedStateHandle.get<String>("addictionId")?.toInt()
        if (savedParam == -1) {
            val startUi = AddictionUI(
                id = null,
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

    private suspend fun saveAddiction() {
        val existingAddiction = getAddictionByTypeUseCase.get().invoke(uiState.value.type)
        if (existingAddiction is RequestResult.Success && existingAddiction.data.id != uiState.value.id) {
            onToastMessageStateChange(resourceManager.getStringById(uikitR.string.addiction_exists_error))
            return
        }
        savaAddictionUseCase.get().invoke(uiState.value)
    }

    private suspend fun initUi(addiction: AddictionUI) {
        _uiState.emit(addiction)
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