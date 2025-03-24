package uz.futuresoft.applockdemo.presentation.activities.main

import uz.futuresoft.applockdemo.presentation.utils.AppInfo

data class MainState(
    val loading: Boolean = false,
    val app: AppInfo = AppInfo.INITIAL,
    val apps: List<AppInfo> = emptyList(),
    val blockedApps: List<String> = emptyList(),
)