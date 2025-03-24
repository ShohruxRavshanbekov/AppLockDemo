package uz.futuresoft.applockdemo.presentation.screens.apps.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import uz.futuresoft.applockdemo.presentation.navigation.Screens
import uz.futuresoft.applockdemo.presentation.screens.apps.AppsScreen
import uz.futuresoft.applockdemo.presentation.screens.apps.AppsViewModel

fun NavGraphBuilder.appsRoute(
    navController: NavHostController,
) {
    composable<Screens.Apps> {
        val viewModel = koinViewModel<AppsViewModel>()

        AppsScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}