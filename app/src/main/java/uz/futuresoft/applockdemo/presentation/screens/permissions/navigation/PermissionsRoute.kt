package uz.futuresoft.applockdemo.presentation.screens.permissions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import uz.futuresoft.applockdemo.presentation.navigation.Screens
import uz.futuresoft.applockdemo.presentation.screens.permissions.PermissionsScreen

fun NavGraphBuilder.permissionsRoute(
    navController: NavHostController,
) {
    composable<Screens.Permissions> {
        PermissionsScreen(navController = navController)
    }
}