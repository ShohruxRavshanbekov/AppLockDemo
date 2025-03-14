package uz.futuresoft.applockdemo.presentation.view_model

import uz.futuresoft.applockdemo.presentation.utils.AppInfo

data class SharedState(
    val app: AppInfo? = null,
    val apps: List<AppInfo> = emptyList(),
    val blockedApps: List<String> = emptyList(),
)