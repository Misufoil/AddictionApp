package dev.misufoil.addictions_data

import dev.misufoil.addictions_data.model.Addiction
import dev.misufoil.database.models.AddictionDBO

internal fun AddictionDBO.toAddiction(): Addiction {
    return Addiction (
        type = type,
        date = date,
        time = time,
        daysPerWeek = daysPerWeek,
        timesInDay = timesInDay
    )
}

internal fun Addiction.toAddictionDBO(): AddictionDBO {
    return AddictionDBO (
        type = type,
        date = date,
        time = time,
        daysPerWeek = daysPerWeek,
        timesInDay = timesInDay
    )
}