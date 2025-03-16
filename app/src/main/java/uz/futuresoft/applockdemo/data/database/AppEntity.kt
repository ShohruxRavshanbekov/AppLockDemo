package uz.futuresoft.applockdemo.data.database

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app")
data class AppEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val packageName: String,
    val icon: Drawable?,
    val locked: Boolean,
)
