package dev.misufoil.core_utils

import java.time.Duration

object MilestonesProvider {
    val milestones = listOf(
        Duration.ofDays(0),
        Duration.ofDays(1),
        Duration.ofDays(3),
        Duration.ofDays(5),
        Duration.ofDays(10),
        Duration.ofDays(30),
        Duration.ofDays(90),
        Duration.ofDays(160),
        Duration.ofDays(360)
    )
}


//object MilestonesMinutesProvider {
//    val milestones = listOf(
//        Duration.ofMinutes(1),
//        Duration.ofMinutes(2),
//        Duration.ofMinutes(3),
//        Duration.ofMinutes(7),
//        Duration.ofMinutes(20),
//        Duration.ofMinutes(99),
//        Duration.ofMinutes(160),
//        Duration.ofMinutes(360)
//    )
//}