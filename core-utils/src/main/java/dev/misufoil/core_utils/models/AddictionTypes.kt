package dev.misufoil.core_utils.models


enum class AddictionTypes(val description: String) {
    ALCOHOL("Alcohol"),
    DRUGS("Drug"),
    GAMBLING("Gambling"),
    SMOKING("Smoking"),
    INTERNET("Internet"),
    FOOD("Food"),
    SHOPPING("Shopping"),
    WORK("Work"),
    VIDEO_GAMES("Video games");

    override fun toString() = description
}
