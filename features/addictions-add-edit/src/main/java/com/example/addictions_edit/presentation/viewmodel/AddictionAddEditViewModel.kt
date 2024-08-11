package com.example.addictions_edit.presentation.viewmodel


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictions_edit.models.AddictionUI
import com.example.addictions_edit.usecase.GetAddictionByIdUseCase
import com.example.addictions_edit.usecase.GetAddictionByTypeUseCase
import com.example.addictions_edit.usecase.SavaAddictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.misufoil.addictions_data.RequestResult
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

@HiltViewModel
internal class AddictionAddEditViewModel @Inject constructor(
    private val getAddictionByIdUseCase: Provider<GetAddictionByIdUseCase>,
    private val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    private val savaAddictionUseCase: Provider<SavaAddictionUseCase>,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State = State.Loading()
        private set

    private val _uiState: MutableStateFlow<AddictionUI> by lazy {
        MutableStateFlow(
            AddictionUI(
                id = null,
                type = context.getString(dev.misufoil.addictions.uikit.R.string.alcohol),
                date = convertDateToString(LocalDate.now()),
                time = getCurrentTimeString(),
                daysPerWeek = 1,
                timesInDay = 1,
            )
        )
    }

    val uiState: StateFlow<AddictionUI> = _uiState.asStateFlow()

    var showBottomSheet by mutableStateOf(false)
        private set

    var toastMessage by mutableStateOf<String?>(null)
        private set

    fun onBottomSheetShow() {
        showBottomSheet = true
    }

    fun onBottomSheetHide() {
        showBottomSheet = false
    }

    var showDateDialog by mutableStateOf(false)
        private set

    fun showDateDialogShow() {
        showDateDialog = true
    }

    fun showDateDialogHide() {
        showDateDialog = false
    }

    var showTimeDialog by mutableStateOf(false)
        private set

    fun showTimeDialogShow() {
        showTimeDialog = true
    }

    fun showTimeDialogHide() {
        showTimeDialog = false
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

    init {
        val savedParam = savedStateHandle.get<String>("addictionId")?.toInt()
        if (savedParam == -1) {
            state = State.Success(_uiState.value)
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

    private suspend fun initUi(addiction: AddictionUI) {
        _uiState.emit(addiction)
    }

    private fun RequestResult<AddictionUI>.toState(): State {
        return when (this) {
            is RequestResult.Error -> State.Error(data)
            is RequestResult.InProgress -> State.Loading(data)
            is RequestResult.Success -> State.Success(data)
        }
    }

    internal sealed class State(open val addiction: AddictionUI?) {
        data object None : State(addiction = null)
        class Loading(addiction: AddictionUI? = null) : State(addiction)
        class Error(addiction: AddictionUI? = null) : State(addiction)
        class Success(override val addiction: AddictionUI) : State(addiction)
    }

    suspend fun saveAddiction() {
        val existingAddiction = getAddictionByTypeUseCase.get().invoke(uiState.value.type)
        if (existingAddiction is RequestResult.Success && existingAddiction.data.id != uiState.value.id) {
            toastMessage =
                context.getString(dev.misufoil.addictions.uikit.R.string.addiction_exists_error)
            return
        }

        savaAddictionUseCase.get().invoke(uiState.value)
    }

    fun clearToastMessage() {
        toastMessage = null
    }
}