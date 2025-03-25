package uz.futuresoft.applockdemo.presentation.screens.permissions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import uz.futuresoft.applockdemo.presentation.navigation.Screens
import uz.futuresoft.applockdemo.presentation.screens.permissions.PermissionsScreen
import uz.futuresoft.applockdemo.presentation.screens.permissions.PermissionsViewModel

fun NavGraphBuilder.permissionsRoute(
    navController: NavHostController,
) {
    composable<Screens.Permissions> {
        val viewModel = koinViewModel<PermissionsViewModel>()

        PermissionsScreen(
            navController = navController,
            viewModel = viewModel,
        )
    }
}