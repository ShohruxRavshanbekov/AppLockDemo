package uz.futuresoft.applockdemo.presentation.screens.permissions

import androidx.lifecycle.ViewModel
import uz.futuresoft.applockdemo.data.SharedPreferencesManager

class PermissionsViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    fun onAction(action: PermissionsAction) {
        when (action) {
            PermissionsAction.PermissionsAllowed ->
                sharedPreferencesManager.setPermissionsState(true)

            else -> Unit
        }
    }

}