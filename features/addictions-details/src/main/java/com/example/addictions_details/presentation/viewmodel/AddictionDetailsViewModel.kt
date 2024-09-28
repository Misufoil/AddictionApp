package com.example.addictions_details.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.usecase.DeleteAddictionUseCase
import com.example.addictions_details.usecase.GetAddictionByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class AddictionDetailsViewModel @Inject constructor(
    private val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    private val deleteAddictionUseCase: Provider<DeleteAddictionUseCase>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State = State.Loading()
        private set

    var showDeleteDialog by mutableStateOf(false)
        private set

    var totalMoneySaved: Double = 0.0
        private set

    var totalCaloriesSaved: Double = 0.0
        private set

    init {
        val addictionId = savedStateHandle.get<String>("addictionId")?.toInt()

        if (addictionId != null) {
            viewModelScope.launch {
                state = State.Loading()
                val result = getAddictionByTypeUseCase.get().invoke(addictionId.toInt())
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

    fun showDeleteDialog() {
        showDeleteDialog = true
    }

    fun hideDeleteDialog() {
        showDeleteDialog = false
    }

    suspend fun deleteAddiction() {
        state.addiction?.let { deleteAddictionUseCase.get().invoke(it) }
        hideDeleteDialog()
    }

    private fun initUi(data: AddictionUI) {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        val startDate = LocalDate.parse(data.date, formatter)
        val currentDate = LocalDate.now()
        val daysPassed = ChronoUnit.DAYS.between(startDate, currentDate).toInt()

        // Вычисление сэкономленных денег и калорий
        totalMoneySaved = daysPassed * data.moneyPerDay
        totalCaloriesSaved = daysPassed * data.caloriesPerDay

        state = State.Success(data)
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