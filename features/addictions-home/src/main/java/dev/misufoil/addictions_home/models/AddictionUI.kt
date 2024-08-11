package dev.misufoil.addictions_home.models

data class AddictionUI(
    val id: Int?,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
)