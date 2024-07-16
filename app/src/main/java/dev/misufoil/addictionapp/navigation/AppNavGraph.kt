package dev.misufoil.addictionapp.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.addictions_details.presentation.AddictionDetailsScreen
import com.example.addictions_edit.presentation.view.AddictionAddEditScreen
import dev.misufoil.addictions_home.presentation.AddictionsHomeScreen
import dev.misufoil.core_utils.Routes.ADD_EDIT_SCREEN
import dev.misufoil.core_utils.Routes.DETAIL_SCREEN
import dev.misufoil.core_utils.Routes.HOME_SCREEN

@ExperimentalMaterial3Api
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
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
                navigateToAddEdit = { type ->
                    navController.navigate("$ADD_EDIT_SCREEN?type=$type")
                },
                navigateToDetails = { type ->
                    navController.navigate("$DETAIL_SCREEN?type=$type")
                }
            )
        }
        composable(
            route = "$DETAIL_SCREEN?type={type}",
            arguments = listOf(
                navArgument(name = "type") {
                    type = NavType.StringType
                    ////Tut
                    defaultValue = ""
                    ////Tut
                }
            )
        ) {
            AddictionDetailsScreen(
                navigateToAddEdit = { type ->
                    navController.navigate("$ADD_EDIT_SCREEN?type=$type")
                },
                onPopBackStack = {
                    navController.popBackStack(HOME_SCREEN, false)
                }
            )

        }
        composable(
            route = "$ADD_EDIT_SCREEN?type={type}",
            arguments = listOf(
                navArgument(name = "type") {
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

















