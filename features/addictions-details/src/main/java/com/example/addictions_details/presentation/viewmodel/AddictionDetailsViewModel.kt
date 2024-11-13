package com.example.addictions_details.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.addictions_details.models.AddictionUI
import com.example.addictions_details.usecase.DeleteAddictionUseCase
import com.example.addictions_details.usecase.GetAddictionByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.misufoil.addictions_data.RequestResult
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class AddictionDetailsViewModel @Inject constructor(
    private val getAddictionByTypeUseCase: Provider<GetAddictionByTypeUseCase>,
    private val deleteAddictionUseCase: Provider<DeleteAddictionUseCase>,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state: State = State.Loading()
        private set

    var showDeleteDialog by mutableStateOf(false)
        private set

    init {
        val addictionId = savedStateHandle.get<String>("addictionId")

        if (addictionId != null) {
            viewModelScope.launch {
                state = State.Loading()
                val result = getAddictionByTypeUseCase.get().invoke(addictionId)
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
        state.addiction?.let { deleteWorkerChain(it.type) }
    }

    private fun deleteWorkerChain(id: String) {
        val result = workManager.cancelUniqueWork(id)
        Log.d("deleteWorker", result.toString())
    }

    private fun initUi(data: AddictionUI) {

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