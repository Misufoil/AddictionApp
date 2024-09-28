package dev.misufoil.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AddictionDBO")
data class AddictionDBO(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("date") val date: String,
    @ColumnInfo("time") val time: String,
    @ColumnInfo("daysPerWeek") val daysPerWeek: Int,
    @ColumnInfo("timesInDay") val timesInDay: Int,
    @ColumnInfo("moneyPerDay") val moneyPerDay: Double,
    @ColumnInfo("caloriesPerDay") val caloriesPerDay: Double,
)