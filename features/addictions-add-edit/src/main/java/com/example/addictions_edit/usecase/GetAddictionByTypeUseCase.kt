package com.example.addictions_edit.usecase

import com.example.addictions_edit.models.AddictionUI
import dev.misufoil.addictions_data.AddictionsRepository
import dev.misufoil.addictions_data.RequestResult
import dev.misufoil.addictions_data.map
import dev.misufoil.addictions_data.model.Addiction
import dev.misufoil.core_utils.models.AddictionTypes
import javax.inject.Inject

internal class GetAddictionByTypeUseCase @Inject constructor(
    private val repository: AddictionsRepository
) {
    suspend operator fun invoke(type: AddictionTypes): RequestResult<AddictionUI> {
        val x = repository.getAddictionByType(type)
            .map { addictions ->
                addictions.toUIAddiction()
            }
        return x
    }
}

private fun Addiction.toUIAddiction(): AddictionUI {
    return AddictionUI(
        type = type,
        date = date,
        time = time,
        daysPerWeek = daysPerWeek,
        timesInDay = timesInDay
    )
}