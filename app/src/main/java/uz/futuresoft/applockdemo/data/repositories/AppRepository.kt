package uz.futuresoft.applockdemo.data.repositories

import kotlinx.coroutines.flow.Flow
import uz.futuresoft.applockdemo.presentation.utils.AppInfo

interface AppRepository {
    fun getApps(): Flow<List<AppInfo>>
    suspend fun getApp(packageName: String): AppInfo
    suspend fun updateLockStatus(packageName: String, lockStatus: Boolean)
}