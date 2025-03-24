package uz.futuresoft.applockdemo.presentation.screens.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.futuresoft.applockdemo.data.repositories.AppRepository

class AppsViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AppsState())
    val uiState: StateFlow<AppsState> = _uiState.asStateFlow()

    init {
        loadApps()
    }

    fun onAction(action: AppsAction) {
        when (action) {
            AppsAction.RefreshData -> loadApps()
            is AppsAction.ChangeAppLockedStatus -> changeAppLockStatus(
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