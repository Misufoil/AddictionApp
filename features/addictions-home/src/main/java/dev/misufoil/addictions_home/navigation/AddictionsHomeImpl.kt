//package dev.misufoil.addictions_home.navigation
//
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.compose.composable
//import com.example.addictions_main_api.AddictionsHomeApi
//import dev.misufoil.addictions_home.presentation.AddictionsHomeScreen
//
//private const val baseRoute = "home"
//
//class AddictionsHomeImpl: AddictionsHomeApi {
//    override val homeRoute: String
//        get() = baseRoute
//
//    override fun registerGraph(
//        navGraphBuilder: NavGraphBuilder,
//        navController: NavController,
//        modifier: Modifier
//    ) {
//        navGraphBuilder.composable(baseRoute) {
//            AddictionsHomeScreen(
//                onNavigateTo = {
//                        navController.navigate("")
//                    }
//            )
//        }
//    }
//}