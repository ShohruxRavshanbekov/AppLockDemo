package uz.futuresoft.applockdemo.presentation.activities.lock

sealed interface LockAction {
    data class GetApp(val packageName: String) : LockAction
}