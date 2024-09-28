package com.example.addictions_details.models

data class AddictionUI(
    val id: Int?,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
    val moneyPerDay: Double,
    val caloriesPerDay: Double,
)

