package com.example.addictions_details.models

internal data class AddictionUI(
    var id: Int?,
    var type: String,
    var date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int
)

