package dev.misufoil.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AddictionDBO")
data class AddictionDBO(
    @PrimaryKey
    @ColumnInfo("id")val id: String,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("date") val date: String,
    @ColumnInfo("time") val time: String,
    @ColumnInfo("daysPerWeek") val daysPerWeek: Int,
    @ColumnInfo("timesInDay") val timesInDay: Int
)