package uz.futuresoft.applockdemo.presentation.view_model

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.futuresoft.applockdemo.presentation.utils.AppInfo
import uz.futuresoft.applockdemo.data.database.AppDatabase
import uz.futuresoft.applockdemo.presentation.utils.toAppEntity
import uz.futuresoft.applockdemo.presentation.utils.toAppInfo

class SharedViewModel(
    private val database: AppDatabase,
) : ViewModel() {
    private val _sharedUiState = MutableStateFlow(SharedState())
    val sharedUiState: StateFlow<SharedState> = _sharedUiState.asStateFlow()

    private val _blockedAppsObserver = MutableLiveData<List<String>>()
    val blockedAppsObserver: LiveData<List<String>> = _blockedAppsObserver

//    private var blockedApps: List<String> = emptyList()

    init {
//        blockedApps = sharedPreferences.getBlockedApps()
//        _sharedUiState.update { it.copy(blockedApps = blockedApps) }
    }

    fun onAction(action: SharedAction) {
        when (action) {
            is SharedAction.GetApps -> getApps(context = action.context)
            is SharedAction.OnChangeAppBlockedStatus -> changeAppLockStatus(
                locked = action.status,
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
                locked = database.appDao.getApp(packageName = it.activityInfo.packageName).locked,
//                locked = sharedPreferences.isAppBlocked(packageName = it.activityInfo.packageName),
            )
        }
        database.appDao.insertAll(mappedApps.map { it.toAppEntity() })
        _sharedUiState.update {
            it.copy(
                apps = database.appDao.getApps().map { appEntity -> appEntity.toAppInfo() }
            )
        }
        return mappedApps
    }

    private fun getApp(context: Context, packageName: String?) {
        _sharedUiState.update { currentState ->
            currentState.copy(
                app = getApps(context = context).find { it.packageName == (packageName ?: "") },
            )
        }
    }

    private fun changeAppLockStatus(locked: Boolean, packageName: String) {
        database.appDao.changeAppLockStatus(packageName = packageName, locked = locked)
        _sharedUiState.update {
            it.copy(
                apps = database.appDao.getApps().map { appEntity -> appEntity.toAppInfo() }
            )
        }
//        val blockedApps = this.blockedApps.toMutableList()
//        if (status) {
//            blockedApps.add(packageName)
//        } else {
//            blockedApps.remove(packageName)
//        }
//        sharedPreferences.saveBlockedApps(apps = blockedApps)
    }
}