package uz.futuresoft.applockdemo.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {
    @Serializable
    data object Apps : Screens

    @Serializable
    data object Permissions : Screens
}