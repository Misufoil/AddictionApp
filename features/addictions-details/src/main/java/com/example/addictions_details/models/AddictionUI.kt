package com.example.addictions_details.models

internal data class AddictionUI(
    var id: Int?,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int
)

