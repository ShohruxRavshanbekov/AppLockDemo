package uz.futuresoft.applockdemo.utils

import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable?,
    val locked: Boolean,
)