package com.example.addictions_edit.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class AddictionUI(
    val id: Int?,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
    val moneyPerDay: Double,
    val caloriesPerDay: Double,
) {
    companion object {
        val empty = AddictionUI(
            id = null,
            type = "",
            date = "",
            time = "",
            daysPerWeek = 0,
            timesInDay = 0,
            moneyPerDay = 0.0,
            caloriesPerDay = 0.0,
        )
    }
}