package uz.futuresoft.applockdemo.view_model

import uz.futuresoft.applockdemo.utils.AppInfo

data class SharedState(
    val app: AppInfo? = null,
    val apps: List<AppInfo> = emptyList(),
    val blockedApps: List<String> = emptyList(),
)