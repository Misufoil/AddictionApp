package dev.misufoil.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AddictionDBO")
data class AddictionDBO(
    @PrimaryKey @ColumnInfo("type") var type: dev.misufoil.core_utils.models.AddictionTypes,
    @ColumnInfo("date") var date: String,
    @ColumnInfo("time") val time: String,
    @ColumnInfo("daysPerWeek") val daysPerWeek: Int,
    @ColumnInfo("timesInDay") val timesInDay: Int
)

