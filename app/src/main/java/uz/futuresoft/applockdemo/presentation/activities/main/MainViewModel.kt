package uz.futuresoft.applockdemo.presentation.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.futuresoft.applockdemo.data.repositories.AppRepository

class MainViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        loadApps()
    }

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.RefreshData -> loadApps()
            is MainAction.ChangeAppLockedStatus -> changeAppLockStatus(
                isLocked = action.status,
                packageName = action.packageName
            )
        }
    }

    private fun loadApps() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.getApps().collect { apps ->
                _uiState.update { currentState ->
                    currentState.copy(loading = false, apps = apps)
                }
            }
        }
    }

    private fun changeAppLockStatus(isLocked: Boolean, packageName: String) {
        viewModelScope.launch {
            appRepository.updateLockStatus(packageName = packageName, lockStatus = isLocked)
            appRepository.getApps().collect { apps ->
                _uiState.update { currentState ->
                    currentState.copy(apps = apps)
                }
            }
        }
    }
}