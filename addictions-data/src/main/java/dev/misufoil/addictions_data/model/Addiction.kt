package dev.misufoil.addictions_data.model

import dev.misufoil.core_utils.models.AddictionTypes

data class Addiction(
    var type: AddictionTypes,
    var date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
)