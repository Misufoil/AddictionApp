package dev.misufoil.addictions_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.addictions_main.models.UIAddictions
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Provider
import javax.inject.Inject

@HiltViewModel
internal class AddictionsMainViewModel @Inject constructor(
    getAllAddictionsUseCase: Provider<GetAllAddictionsUseCase>
) : ViewModel() {

    val state: StateFlow<State> = getAllAddictionsUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}


private fun RequestResult<List<UIAddictions>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Succeess(checkNotNull(data))
    }
}

sealed class State {
    object None : State()
    class Loading(val addictions: List<UIAddictions>? = null) : State()
    class Error(val addictions: List<UIAddictions>? = null) : State()
    class Succeess(val addictions: List<UIAddictions>) : State()
}