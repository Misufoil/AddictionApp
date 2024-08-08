package com.example.addictions_details.usecase

import com.example.addictions_details.models.AddictionUI
import dev.misufoil.addictions_data.AddictionsRepository
import dev.misufoil.addictions_data.model.Addiction
import javax.inject.Inject

internal class DeleteAddictionUseCase @Inject constructor(
    private val repository: AddictionsRepository
) {
    suspend operator fun invoke(addictionUI: AddictionUI) {
        val addiction = addictionUI.toAddiction()
        repository.removeAddictionToLocalDb(addiction)
    }
}

internal fun AddictionUI.toAddiction(): Addiction {
    return Addiction(
        id = id,
        type = type,
        date = date,
        time = time,
        daysPerWeek = daysPerWeek,
        timesInDay = timesInDay
    )
}

//internal class SavaAddictionUseCase @Inject constructor(
//    private val repository: AddictionsRepository
//) {
//    suspend operator fun invoke(addictionUI: AddictionUI) {
//        val addiction = addictionUI.toAddiction()
//        repository.savaAddictionToLocalDb(addiction)
//    }
//}

