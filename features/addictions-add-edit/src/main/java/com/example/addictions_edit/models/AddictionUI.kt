package com.example.addictions_edit.models

internal data class AddictionUI(
    val id: Int?,
    var type: String,
    var date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
)