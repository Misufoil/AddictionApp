package com.example.addictions_details.models

import dev.misufoil.core_utils.models.AddictionTypes

internal data class AddictionUI(
    var type: AddictionTypes,
    var date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int
)

