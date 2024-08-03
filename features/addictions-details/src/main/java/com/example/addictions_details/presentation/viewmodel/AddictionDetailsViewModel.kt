package com.example.addictions_details.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.usecase.DeleteAddictionUseCase
import com.example.addictions_details.usecase.GetAddictionByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.core_utils.models.AddictionTypes
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class AddictionDetailsViewModel @Inject constructor(
    val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    val deleteAddictionUseCase: Provider<DeleteAddictionUseCase>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    var state: State = State.Loading()
        private set

    init {
        val savedType = savedStateHandle.get<String>("type")!!

        if (savedType.isNotEmpty()) {
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

    private fun initUi(data: AddictionUI) {

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

    suspend fun deleteAddiction() {
        state.addiction?.let { deleteAddictionUseCase.get().invoke(it) }
    }

}
