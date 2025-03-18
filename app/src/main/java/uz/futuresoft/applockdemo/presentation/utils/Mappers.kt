package uz.futuresoft.applockdemo.presentation.utils

import uz.futuresoft.applockdemo.data.database.AppEntity

fun AppInfo.toAppEntity(): AppEntity {
    return AppEntity(
        packageName = this.packageName,
        name = this.name,
        locked = this.locked,
    )
}

fun AppEntity.toAppInfo(): AppInfo {
    return AppInfo(
        packageName = this.packageName,
        name = this.name,
        locked = this.locked,
    )
}