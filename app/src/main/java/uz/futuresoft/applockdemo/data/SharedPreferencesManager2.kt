package uz.futuresoft.applockdemo.data

import android.content.Context
import android.content.SharedPreferences
import uz.futuresoft.applockdemo.presentation.App

object SharedPreferencesManager2 {
    private const val NAME = "app_prefs"
    private const val KEY_BLOCKED_APPS = "blocked_apps"

    private val sharedPreferences: SharedPreferences by lazy {
        App.instance.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
    private val editor = sharedPreferences.edit()

    fun saveBlockedApps(apps: List<String>) {
        editor.putStringSet(KEY_BLOCKED_APPS, apps.toSet())?.apply()
    }

    fun getBlockedApps(): List<String> {
        return sharedPreferences.getStringSet(KEY_BLOCKED_APPS, emptySet())?.toList()
            ?: emptyList()
    }

    fun isAppBlocked(packageName: String): Boolean {
        return getBlockedApps().contains(packageName)
    }
}