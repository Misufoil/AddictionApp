package dev.misufoil.addictions_data.model

data class Addiction(
    val id: Int?,
    var type: String,
    var date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
)