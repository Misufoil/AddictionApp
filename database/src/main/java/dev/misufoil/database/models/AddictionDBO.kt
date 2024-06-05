package dev.misufoil.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "AddictionDBO")
data class AddictionDBO(
    @PrimaryKey @ColumnInfo("type") var type: dev.misufoil.core_utils.models.AddictionTypes,
    @ColumnInfo("date") var date: LocalDate,
    @ColumnInfo("time") val time: LocalTime,
    @ColumnInfo("daysPerWeek") val daysPerWeek: dev.misufoil.core_utils.models.DaysPerWeek,
    @ColumnInfo("timesInDay") val timesInDay: Int
)

