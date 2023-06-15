package cz.pochoto.mydemoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.pochoto.mydemoapp.presentation.DetailScreen
import cz.pochoto.mydemoapp.presentation.MainScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(navArgument(name = "id") {
                type = NavType.IntType
            })
        ) {
            DetailScreen(navController = navController)
        }
    }
}