package dev.misufoil.addictionapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.misufoil.addictions_home.presentation.AddictionsHomeScreen

//@Composable
//fun AppNavGraph(
//    modifier: Modifier = Modifier,
//    navController: NavHostController
//) {
//    NavHost(navController = navController, startDestination = HOME_SCREEN) {
//        composable(route = HOME_SCREEN) {
//            AddictionsHomeScreen(
//                navigateTo = {
//                    navController.navigate("")
//                }
//            )
//        }
//        composable(route = DETAIL_SCREEN) {
//
//        }
//        composable(route = EDIT_SCREEN) {
//
//        }
//    }
//
//}
//
//internal const val HOME_SCREEN = "home"
//internal const val EDIT_SCREEN = "edit"
//internal const val DETAIL_SCREEN = "details"