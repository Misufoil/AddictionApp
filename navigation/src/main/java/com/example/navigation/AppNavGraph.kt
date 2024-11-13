package com.example.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.addictions_details.presentation.view.AddictionDetailsScreen
import com.example.addictions_edit.presentation.view.AddictionAddEditScreen
import dev.misufoil.addictions_home.presentation.view.AddictionsHomeScreen
import dev.misufoil.core_utils.Routes.ADD_EDIT_SCREEN
import dev.misufoil.core_utils.Routes.DETAIL_SCREEN
import dev.misufoil.core_utils.Routes.HOME_SCREEN

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startParams: String
) {
    val repetitionState = remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        enterTransition = {
            // you can change whatever you want transition
            EnterTransition.None
        },
        exitTransition = {
            // you can change whatever you want transition
            ExitTransition.None
        }
    ) {
        composable(route = HOME_SCREEN) {
            AddictionsHomeScreen(
                navigateToAddEdit = { addictionId ->
                    navController.navigate("$ADD_EDIT_SCREEN?addictionId=$addictionId")
                },
                navigateToDetails = { addictionId ->
                    navController.navigate("$DETAIL_SCREEN?addictionId=$addictionId")
                }
            )

            if (!repetitionState.value && startParams.isNotEmpty()) {
                LaunchedEffect(startParams) {
                    navController.navigate("$DETAIL_SCREEN?addictionId=$startParams")
                    Log.e(DETAIL_SCREEN, startParams)
                    repetitionState.value = true
                }
            }

        }

        composable(
            route = "$DETAIL_SCREEN?addictionId={addictionId}",
            arguments = listOf(
                navArgument(name = "addictionId") {
                    type = NavType.StringType
                    ////Tut
                    defaultValue = ""
                    ////Tut
                }
            )
        ) {
            AddictionDetailsScreen(
                navigateToAddEdit = { addictionId ->
                    navController.navigate("$ADD_EDIT_SCREEN?addictionId=$addictionId")
                },
                onPopBackStack = {
                    navController.popBackStack(HOME_SCREEN, false)
                }
            )

        }
        composable(
            route = "$ADD_EDIT_SCREEN?addictionId={addictionId}",
            arguments = listOf(
                navArgument(name = "addictionId") {
                    type = NavType.StringType
                    ////Tut
                    defaultValue = ""
                    ////Tut
                }
            )
        ) { backStackEntry ->
//            val type = backStackEntry.arguments?.getString("type") ?: ""
            AddictionAddEditScreen(onPopBackStack = {
                navController.popBackStack(HOME_SCREEN, false)
            })
        }
    }

}