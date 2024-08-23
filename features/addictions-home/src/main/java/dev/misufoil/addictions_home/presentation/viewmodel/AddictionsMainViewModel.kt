package dev.misufoil.addictions_home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.addictions_home.models.AddictionUI
import dev.misufoil.addictions_home.usecase.DeleteAddictionUseCase
import dev.misufoil.addictions_home.usecase.GetAllAddictionsUseCase
import dev.misufoil.addictions_home.usecase.InsertAddictionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class AddictionsMainViewModel @Inject constructor(
    getAllAddictionsUseCase: Provider<GetAllAddictionsUseCase>,
    val deleteAddictionUseCase: Provider<DeleteAddictionUseCase>,
    val insertAddictionUseCase: Provider<InsertAddictionUseCase>,
) : ViewModel() {

    val state: StateFlow<State> = getAllAddictionsUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    private var lastDeletedAddiction: AddictionUI? = null

    fun deleteAddiction(addiction: AddictionUI) {
        viewModelScope.launch {
            deleteAddictionUseCase.get().invoke(addiction)
            lastDeletedAddiction = addiction
        }

    }

    fun undoDelete() {
        viewModelScope.launch {
            lastDeletedAddiction?.let {
                insertAddictionUseCase.get().invoke(it)
            }
            lastDeletedAddiction = null
        }
    }
}

internal sealed class State(open val addictions: List<AddictionUI>?) {
    data object None : State(addictions = null)
    class Loading(addictions: List<AddictionUI>? = null) : State(addictions)
    class Error(addictions: List<AddictionUI>? = null) : State(addictions)
    class Success(override val addictions: List<AddictionUI>) : State(addictions)
}

private fun RequestResult<List<AddictionUI>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(data)
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}