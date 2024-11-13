package dev.misufoil.addictions_home.usecase

import dev.misufoil.addictions_data.AddictionsRepository
import dev.misufoil.addictions_data.model.Addiction
import dev.misufoil.addictions_home.models.AddictionUI
import javax.inject.Inject

internal class DeleteAddictionUseCase @Inject constructor(
    private val repository: AddictionsRepository
) {
    suspend operator fun invoke(addictionUI: AddictionUI) {
        val addiction = addictionUI.toAddiction()
        repository.removeAddictionToLocalDb(addiction)
    }
}

private fun AddictionUI.toAddiction(): Addiction {
    return Addiction(
        id = id,
        type = type,
        date = date,
        time = time,
        daysPerWeek = daysPerWeek,
        timesInDay = timesInDay
    )
}