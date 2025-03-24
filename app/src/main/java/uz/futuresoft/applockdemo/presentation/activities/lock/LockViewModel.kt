package uz.futuresoft.applockdemo.presentation.activities.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.futuresoft.applockdemo.data.database.AppDatabase
import uz.futuresoft.applockdemo.presentation.utils.toAppInfo

class LockViewModel(private val database: AppDatabase) : ViewModel() {
    private val _uiState = MutableStateFlow(LockState())
    val uiState: StateFlow<LockState> = _uiState.asStateFlow()

    fun onAction(action: LockAction) {
        when (action) {
            is LockAction.GetApp -> getApp(action.packageName)
        }
    }

    private fun getApp(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val app = database.appDao.getApp(packageName)
            _uiState.update { it.copy(app = app.toAppInfo()) }
        }
    }
}