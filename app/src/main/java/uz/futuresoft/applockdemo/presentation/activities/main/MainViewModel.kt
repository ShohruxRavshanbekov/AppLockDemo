package uz.futuresoft.applockdemo.presentation.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.futuresoft.applockdemo.data.SharedPreferencesManager
import uz.futuresoft.applockdemo.data.repositories.AppRepository
import uz.futuresoft.applockdemo.presentation.navigation.Screens

class MainViewModel(private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                startDestination = if (sharedPreferencesManager.arePermissionsAllowed()) Screens.Apps else Screens.Permissions
            )
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.PermissionsAllowed -> changePermissionsState()
        }
    }

    private fun changePermissionsState() {
        sharedPreferencesManager.setPermissionsState(permissionsAllowed = true)
    }
}