package uz.futuresoft.applockdemo.presentation.screens.apps

import uz.futuresoft.applockdemo.presentation.screens.apps.utils.AppInfo

data class AppsState(
    val loading: Boolean = false,
    val app: AppInfo = AppInfo.INITIAL,
    val apps: List<AppInfo> = emptyList(),
    val blockedApps: List<String> = emptyList(),
)