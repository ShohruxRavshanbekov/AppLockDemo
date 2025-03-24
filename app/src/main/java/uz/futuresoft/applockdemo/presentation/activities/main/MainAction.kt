package uz.futuresoft.applockdemo.presentation.activities.main

sealed interface MainAction {
    data object RefreshData : MainAction
    data class ChangeAppLockedStatus(val status: Boolean, val packageName: String) : MainAction
}