package com.example.addictions_details.models

data class AddictionUI(
    val id: String,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int
)

