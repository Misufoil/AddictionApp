package com.example.addictions_details.usecase

import com.example.addictions_details.models.AddictionUI
import dev.misufoil.addictions_data.AddictionsRepository
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.addictions_data.map
import dev.misufoil.addictions_data.model.Addiction
import javax.inject.Inject

internal class GetAddictionByTypeUseCase @Inject constructor(
    private val repository: AddictionsRepository
) {
    suspend operator fun invoke(id: String): RequestResult<AddictionUI> {
        val x = repository.getAddictionById(id)
            .map { addictions ->
                addictions.toUIAddiction()
            }
        return x
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