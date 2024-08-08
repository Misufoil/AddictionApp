package dev.misufoil.addictions_home.usecase

import dev.misufoil.addictions_data.AddictionsRepository
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.addictions_data.map
import dev.misufoil.addictions_data.model.Addiction
import dev.misufoil.addictions_home.models.AddictionUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllAddictionsUseCase @Inject constructor(
    private val repository: AddictionsRepository
) {
    operator fun invoke(): Flow<RequestResult<List<AddictionUI>>> {
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { addictions ->
                    addictions.map { it.toUIAddiction() }
                }
            }
    }
}

private fun Addiction.toUIAddiction(): AddictionUI {
    return AddictionUI(
        id = id,
        type = type,
        date = date,
        time = time,
        daysPerWeek = daysPerWeek,
        timesInDay = timesInDay
    )
}