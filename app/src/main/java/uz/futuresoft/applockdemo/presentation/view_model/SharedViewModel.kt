package uz.futuresoft.applockdemo.presentation.view_model

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.futuresoft.applockdemo.presentation.utils.AppInfo
import uz.futuresoft.applockdemo.data.SharedPreferencesManager

class SharedViewModel: ViewModel() {
//    private val _sharedUiState = MutableSharedFlow<SharedState>()
//    val sharedUiState: SharedFlow<SharedState> = _sharedUiState.asSharedFlow()

    private val _sharedUiState = MutableStateFlow(SharedState())
    val sharedUiState: StateFlow<SharedState> = _sharedUiState.asStateFlow()

    private val _blockedAppsObserver = MutableLiveData<List<String>>()
    val blockedAppsObserver: LiveData<List<String>> = _blockedAppsObserver

    private var blockedApps: List<String> = emptyList()

    init {
        blockedApps = SharedPreferencesManager.getBlockedApps()
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
                locked = SharedPreferencesManager.isAppBlocked(packageName = it.activityInfo.packageName),
//                locked = sharedPreferencesManager.isAppBlocked(packageName = it.activityInfo.packageName),
            )
        }
        _sharedUiState.update { it.copy(apps = mappedApps) }
        return mappedApps
    }

    private fun getApp(context: Context, packageName: String?) {
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
        SharedPreferencesManager.saveBlockedApps(apps = blockedApps)
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val context =
//                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
//                val sharedPreferencesManager = SharedPreferencesManager2(context = context)
//                SharedViewModel(sharedPreferencesManager = sharedPreferencesManager)
//            }
//        }
//    }
}