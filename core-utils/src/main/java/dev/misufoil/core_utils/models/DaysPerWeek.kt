package dev.misufoil.core_utils.models

enum class DaysPerWeek(var day: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);

    override fun toString() = "$day"

    companion object {
        fun fromInt(value: Int): DaysPerWeek? {
            return entries.find { it.day == value }
        }
    }
}
