package dev.misufoil.addictions_data.model

import dev.misufoil.core_utils.models.AddictionTypes
import dev.misufoil.core_utils.models.DaysPerWeek
import java.time.LocalDate
import java.time.LocalTime

data class Addiction(
    var type: AddictionTypes,
    var date: LocalDate,
    val time: LocalTime,
    val daysPerWeek: DaysPerWeek,
    val timesInDay: Int,
)