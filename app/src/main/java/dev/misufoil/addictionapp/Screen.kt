package dev.misufoil.addictionapp

sealed class Screen(val route: String) {
    object AddictionsMainScreen : Screen("addictions_main_screen")
    object AddictionsEditScreen : Screen("addictions_main_screen")
}