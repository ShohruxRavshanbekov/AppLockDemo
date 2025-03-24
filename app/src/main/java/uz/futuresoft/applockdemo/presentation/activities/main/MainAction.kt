package uz.futuresoft.applockdemo.presentation.activities.main

sealed interface MainAction {
    data object PermissionsAllowed : MainAction
}