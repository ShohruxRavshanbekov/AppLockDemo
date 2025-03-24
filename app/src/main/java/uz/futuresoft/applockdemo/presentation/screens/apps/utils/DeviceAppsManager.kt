package uz.futuresoft.applockdemo.presentation.screens.apps.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo

class DeviceAppsManager(private val context: Context) {
    fun getApps(): List<AppInfo> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val apps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        return apps.map {
            AppInfo(
                name = it.loadLabel(packageManager).toString(),
                packageName = it.activityInfo.packageName,
                locked = false,
            )
        }
    }
}