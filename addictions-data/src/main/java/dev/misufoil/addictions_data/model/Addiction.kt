package dev.misufoil.addictions_data.model

data class Addiction(
    val id:String,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
)