package uz.futuresoft.applockdemo.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import uz.futuresoft.applockdemo.data.database.AppDao
import uz.futuresoft.applockdemo.data.database.AppDatabase
import uz.futuresoft.applockdemo.presentation.utils.AppInfo
import uz.futuresoft.applockdemo.presentation.utils.DeviceAppsManager
import uz.futuresoft.applockdemo.presentation.utils.toAppEntity
import uz.futuresoft.applockdemo.presentation.utils.toAppInfo

class AppRepositoryImpl(
    private val appDao: AppDao,
    private val deviceAppsManager: DeviceAppsManager,
) : AppRepository {
    override fun getApps(): Flow<List<AppInfo>> = flow {
        val existingApps = appDao.getApps().associateBy { it.packageName }
        val newApps = deviceAppsManager.getApps().filter { it.packageName !in existingApps }
        val removedApps =
            existingApps.keys - deviceAppsManager.getApps().map { it.packageName }.toSet()
        appDao.run {
            if (newApps.isNotEmpty()) appDao.insertApps(apps = newApps.map { it.toAppEntity() })
            if (removedApps.isNotEmpty()) removedApps.forEach { appDao.deleteApp(packageName = it) }
        }
        emit(appDao.getApps().map { it.toAppInfo() })
    }

    override suspend fun getApp(packageName: String): AppInfo {
        return appDao.getApp(packageName).toAppInfo()
    }

    override suspend fun updateLockStatus(packageName: String, lockStatus: Boolean) {
        appDao.updateLockStatus(packageName = packageName, locked = lockStatus)
    }
}