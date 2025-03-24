package uz.futuresoft.applockdemo.presentation.activities.main

import uz.futuresoft.applockdemo.presentation.navigation.Screens

data class MainState(
    val startDestination: Screens = Screens.Permissions,
)