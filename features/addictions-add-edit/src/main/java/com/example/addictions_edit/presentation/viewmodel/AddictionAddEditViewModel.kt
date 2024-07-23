package com.example.addictions_edit.presentation.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictions_edit.models.AddictionUI
import com.example.addictions_edit.usecase.GetAddictionByTypeUseCase
import com.example.addictions_edit.usecase.SavaAddictionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.core_utils.date_time_utils.convertDateToString
import dev.misufoil.core_utils.date_time_utils.convertLongToStringDate
import dev.misufoil.core_utils.date_time_utils.formatTime
import dev.misufoil.core_utils.date_time_utils.getCurrentTimeString
import dev.misufoil.core_utils.models.AddictionTypes
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider

// если будет ошибка убрать internal
@HiltViewModel
internal class AddictionAddEditViewModel @Inject constructor(
    val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    val savaAddictionUseCase: Provider<SavaAddictionUseCase>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State = State.Loading()
        private set

//    var addiction by mutableStateOf<AddictionUI?>(null)
//        private set

    var type by mutableStateOf(AddictionTypes.ALCOHOL)
        private set

    var date by mutableStateOf(convertDateToString(LocalDate.now()))
        private set

    var time by mutableStateOf(getCurrentTimeString())
        private set

    var daysPerWeek by mutableStateOf(1)
        private set

    var timesInDay by mutableStateOf(1)
        private set

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

    private fun <T> updateState(update: (T) -> Unit): (T) -> Unit = {
        update(it)
    }

    val onTypeChange = updateState<AddictionTypes> { this.type = it }
    val onDateChange = updateState<Long> { this.date = convertLongToStringDate(it) }
    val onTimeChange = updateState<Pair<Int, Int>> { this.time = formatTime(it.first, it.second) }
    val onDaysPerWeekChange = updateState<Int> { this.daysPerWeek = it }
    val onTimesInDayChange = updateState<Int> { this.timesInDay = it }

    init {
        val savedType = savedStateHandle.get<String>("type")!!

        if (savedType == "Add") {
            state = State.Success(
                AddictionUI(
                    type,
                    date,
                    time,
                    daysPerWeek,
                    timesInDay
                )
            )
            //initUi()
        } else if (savedType.isNotEmpty()) {
//            viewModelScope.launch {
//                val addictionTypes = AddictionTypes.fromDescription(savedType)!!
//                //addiction = getAddictionByTypeUseCase.get().invoke(addictionTypes).toState()
//                state = getAddictionByTypeUseCase.get().invoke(addictionTypes).toState()
//            }
//            initUi()
            viewModelScope.launch {
                state = State.Loading()
                val addictionTypes = AddictionTypes.fromDescription(savedType)
                if (addictionTypes != null) {
                    val result = getAddictionByTypeUseCase.get().invoke(addictionTypes)
                    state = result.toState()
                    if (result is RequestResult.Success) {
                        //addiction = result.data
                        initUi(result.data)
                    } else {
                        // Logging for debugging
                        println("Error fetching data: $result")
                    }
                } else {
                    state = State.Error() // Handle the error if the type is not found
                }
            }
        } else {
            state = State.Error()
        }
    }

    private fun initUi(addiction: AddictionUI) {
        type = addiction.type
        date = addiction.date
        time = addiction.time
        daysPerWeek = addiction.daysPerWeek
        timesInDay = addiction.timesInDay
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
        val saveAddiction = AddictionUI(
            type = type,
            date = date,
            time = time,
            daysPerWeek = daysPerWeek,
            timesInDay = timesInDay
        )
        savaAddictionUseCase.get().invoke(saveAddiction)
    }


    //    fun initUi() {
//        state.addiction.let {
//            if (it != null) {
//                type = it.type
//                date = it.date
//                time = it.time
//                daysPerWeek = it.daysPerWeek
//                timesInDay = it.timesInDay.toString()
//            }
//        }
//    }

//    private fun getCurrentDateToString(): String {
//
////        val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
////        return currentDate.format(dateFormatter)
//        val currentDate = LocalDate.now()
//        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
//        return currentDate.format(dateFormatter)
//
//
////        return SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(currentDate)
//    }

//    fun validateInput(): Boolean {
//        return when {
//            timesInDay.isEmpty() -> {
//                toastMessage = "Поле 'Кол-во в день' не может быть пустым"
//                false
//            }
//            !timesInDay.all { it.isDigit() } -> {
//                toastMessage = "Поле 'Кол-во в день' должно содержать только цифры"
//                false
//            }
//            else -> {
//                toastMessage = null
//                true
//            }
//        }
//    }

//    suspend fun saveAddiction(onSuccess: () -> Unit) {
//        if (!validateInput()) return
//
//        val saveAddiction = AddictionUI(
//            type = type,
//            date = date,
//            time = time,
//            daysPerWeek = daysPerWeek,
//            timesInDay = timesInDay.toInt()
//        )
//        savaAddictionUseCase.get().invoke(saveAddiction)
//        onSuccess()
//    }
//
//    fun clearToastMessage() {
//        toastMessage = null
//    }
}