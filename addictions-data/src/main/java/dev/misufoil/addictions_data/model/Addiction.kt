package dev.misufoil.addictions_data.model

data class Addiction(
    val id: Int?,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
    val moneyPerDay: Double,
    val caloriesPerDay: Double,
)