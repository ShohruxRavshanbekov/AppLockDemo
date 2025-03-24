package uz.futuresoft.applockdemo.presentation.screens.apps

sealed interface AppsAction {
    data object RefreshData : AppsAction
    data class ChangeAppLockedStatus(val status: Boolean, val packageName: String) : AppsAction
}