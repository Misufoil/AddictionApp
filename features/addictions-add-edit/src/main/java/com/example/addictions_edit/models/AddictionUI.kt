package com.example.addictions_edit.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class AddictionUI(
    val id: String,
    val type: String,
    val date: String,
    val time: String,
    val daysPerWeek: Int,
    val timesInDay: Int,
) {
    companion object {
        val empty = AddictionUI(
            id = "",
            type = "",
            date = "",
            time = "",
            daysPerWeek = 0,
            timesInDay = 0,
        )
    }
}