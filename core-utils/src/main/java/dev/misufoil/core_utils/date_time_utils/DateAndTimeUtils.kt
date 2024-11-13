package dev.misufoil.core_utils.date_time_utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun convertStringTimeToLong(timeStr: String): Long {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.parse(timeStr, timeFormatter)
    // Используем текущую дату для создания LocalDateTime
    val currentDate = LocalDate.now()
    val dateTime = LocalDateTime.of(currentDate, time)
    // Преобразуем в миллисекунды с начала эпохи
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun stringDateTimeToLocalDateTime(date: String, time: String): LocalDateTime {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val parseDate = LocalDate.parse(date, dateFormatter)
    val parseTime = LocalTime.parse(time, timeFormatter)
    val addictionDate = LocalDateTime.of(parseDate, parseTime)
    return addictionDate
}

fun getCurrentTimeString(): String {
    val currentTime = LocalTime.now()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentTime.format(timeFormatter)
}

fun formatTime(hour: Int, minute: Int): String {
    return String.format(locale = Locale.ENGLISH, "%02d:%02d", hour, minute)
}

fun convertDateToString(date: LocalDate): String {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return date.format(dateFormatter)
}

fun convertLongToStringDate(time: Long): String {
    //    val date = Date(time)
    //    val format = SimpleDateFormat.getDateInstance()
    //    return format.format(date)

    //val instant = Instant.ofEpochMilli(time)
    // val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    //val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // return date.format(dateFormatter)}

    val instant = Instant.ofEpochMilli(time)
    val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    return convertDateToString(date)
}

fun convertStringDateToLong(strDate: String): Long {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    val date = LocalDate.parse(strDate, dateFormatter)
    val dateTime = date.atStartOfDay()
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

//        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
//        //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.getDefault())
//        //val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.getDefault())

//        val time = LocalTime.parse(addiction.time, timeFormatter)
}


fun convertStringDateTimeToLong(dateStr: String, timeStr: String): Long {
    val dateFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG) // Убедитесь, что формат даты соответствует вашему формату
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")


    val date = LocalDate.parse(dateStr, dateFormatter)
    val time = LocalTime.parse(timeStr, timeFormatter)

    val dateTime = LocalDateTime.of(date, time)
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}