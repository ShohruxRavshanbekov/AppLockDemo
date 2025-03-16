package uz.futuresoft.applockdemo.presentation.utils

import uz.futuresoft.applockdemo.data.database.AppEntity

fun AppEntity.toAppInfo(): AppInfo {
    return AppInfo(
        name = this.name,
        packageName = this.packageName,
        icon = this.icon,
        locked = this.locked,
    )
}