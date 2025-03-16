package uz.futuresoft.applockdemo.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT * FROM app")
    fun getApps(): List<AppEntity>

    @Query("SELECT * FROM app WHERE packageName LIKE :packageName")
    fun getApp(packageName: String): AppEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg app: AppEntity)
}