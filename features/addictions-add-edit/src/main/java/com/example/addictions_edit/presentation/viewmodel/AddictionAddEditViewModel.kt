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
import dev.misufoil.core_utils.models.AddictionTypes
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
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



    internal fun convertDateToString(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        return date.format(dateFormatter)
    }

    internal fun convertLongToStringDate(time: Long): String {
        //    val date = Date(time)
        //    val format = SimpleDateFormat.getDateInstance()
        //    return format.format(date)

        //val instant = Instant.ofEpochMilli(time)
        // val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        //val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // return date.format(dateFormatter)}

        val instant = Instant.ofEpochMilli(time)
        val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        return convertDateToString(date)
    }

    internal fun convertStringDateToLong(strTime: String): Long {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        val date = LocalDate.parse(strTime, dateFormatter)
        val dateTime = date.atStartOfDay()
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

//        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
//        //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.getDefault())
//        //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.getDefault())

//        val time = LocalTime.parse(addiction.time, timeFormatter)
    }

    internal fun convertStringTimeToLong(timeStr: String): Long {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = LocalTime.parse(timeStr, timeFormatter)
        // Используем текущую дату для создания LocalDateTime
        val currentDate = LocalDate.now()
        val dateTime = LocalDateTime.of(currentDate, time)
        // Преобразуем в миллисекунды с начала эпохи
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun getCurrentTimeString(): String {
        val currentTime = LocalTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(timeFormatter)
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return String.format(locale = Locale.ENGLISH, "%02d:%02d", hour, minute)
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