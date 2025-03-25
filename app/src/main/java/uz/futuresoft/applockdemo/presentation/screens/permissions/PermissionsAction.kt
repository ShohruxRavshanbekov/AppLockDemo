package uz.futuresoft.applockdemo.presentation.screens.permissions

sealed interface PermissionsAction {
    data object RequestUsageAccessPermission : PermissionsAction
    data object RequestOverlayPermission : PermissionsAction
    data object RequestNotificationPermission : PermissionsAction
    data object NavigateToAppsScreen : PermissionsAction
    data object PermissionsAllowed : PermissionsAction
}