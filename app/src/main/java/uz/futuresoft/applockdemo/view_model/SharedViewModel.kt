package uz.futuresoft.applockdemo.view_model

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.futuresoft.applockdemo.utils.AppInfo
import uz.futuresoft.applockdemo.utils.SharedPreferencesManager

class SharedViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {
//    private val _sharedUiState = MutableSharedFlow<SharedState>()
//    val sharedUiState: SharedFlow<SharedState> = _sharedUiState.asSharedFlow()

    private val _sharedUiState = MutableStateFlow(SharedState())
    val sharedUiState: StateFlow<SharedState> = _sharedUiState.asStateFlow()

    private var blockedApps: List<String> = emptyList()

    init {
        blockedApps = sharedPreferencesManager.getBlockedApps()
//        viewModelScope.launch {
//            _sharedUiState.emit(SharedState(blockedApps = blockedApps))
//        }
        _sharedUiState.update { it.copy(blockedApps = blockedApps) }
    }

    fun onAction(action: SharedAction) {
        when (action) {
            is SharedAction.GetApps -> getApps(context = action.context)
            is SharedAction.OnChangeAppBlockedStatus -> changeAppBlockedStatus(
                status = action.status,
                packageName = action.packageName
            )

            is SharedAction.GetApp -> getApp(
                context = action.context,
                packageName = action.packageName
            )
        }
    }

    private fun getApps(context: Context): List<AppInfo> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val apps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        val mappedApps = apps.map {
            AppInfo(
                name = it.loadLabel(packageManager).toString(),
                packageName = it.activityInfo.packageName,
                icon = it.loadIcon(packageManager),
                locked = sharedPreferencesManager.isAppBlocked(packageName = it.activityInfo.packageName),
            )
        }
//        viewModelScope.launch {
//            _sharedUiState.emit(SharedState(apps = mappedApps))
//        }
        _sharedUiState.update { it.copy(apps = mappedApps) }
        return mappedApps
    }

    private fun getApp(context: Context, packageName: String?) {
//        viewModelScope.launch {
//            _sharedUiState.emit(
//                SharedState(
//                    app = getApps(context = context).find {
//                        it.packageName == (packageName ?: "")
//                    },
//                )
//            )
//        }
        _sharedUiState.update { currentState ->
            currentState.copy(
                app = getApps(context = context).find { it.packageName == (packageName ?: "") },
            )
        }
        Log.d("AAAAA", "getApp: ${_sharedUiState.value.app}")
    }

    private fun changeAppBlockedStatus(status: Boolean, packageName: String) {
        val blockedApps = this.blockedApps.toMutableList()
        if (status) {
            blockedApps.add(packageName)
        } else {
            blockedApps.remove(packageName)
        }
        sharedPreferencesManager.saveBlockedApps(apps = blockedApps)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                val sharedPreferencesManager = SharedPreferencesManager(context = context)
                SharedViewModel(sharedPreferencesManager = sharedPreferencesManager)
            }
        }
    }
}