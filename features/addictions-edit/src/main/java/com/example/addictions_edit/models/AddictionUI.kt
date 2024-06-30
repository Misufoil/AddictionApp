package com.example.addictions_edit.models

import dev.misufoil.core_utils.models.AddictionTypes
import dev.misufoil.core_utils.models.DaysPerWeek
import java.time.LocalDate
import java.time.LocalTime

internal data class AddictionUI (
    var type: AddictionTypes,
    var date: LocalDate,
    val time: LocalTime,
    val daysPerWeek: DaysPerWeek,
    val timesInDay: Int,
)