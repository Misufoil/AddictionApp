package dev.misufoil.core_utils.models

enum class DaysPerWeek(val days: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);

    override fun toString() = "$days"
}
