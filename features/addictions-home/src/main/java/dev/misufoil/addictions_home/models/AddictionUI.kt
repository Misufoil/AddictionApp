package dev.misufoil.addictions_home.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class AddictionUI(
    val id: String,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
)