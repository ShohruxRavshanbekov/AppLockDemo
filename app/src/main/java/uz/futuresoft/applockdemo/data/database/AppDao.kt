package uz.futuresoft.applockdemo.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT * FROM apps")
    suspend fun getApps(): List<AppEntity>

    @Query("SELECT * FROM apps WHERE packageName LIKE :packageName")
    suspend fun getApp(packageName: String): AppEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<AppEntity>)

    @Query("UPDATE apps SET locked = :locked WHERE packageName = :packageName")
    suspend fun updateLockStatus(packageName: String, locked: Boolean)

    @Query("DELETE FROM apps WHERE packageName = :packageName")
    suspend fun deleteApp(packageName: String)
}