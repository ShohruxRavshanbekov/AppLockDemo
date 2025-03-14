package uz.futuresoft.applockdemo.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager2 {
    private const val NAME = "app_prefs"
    private const val KEY_BLOCKED_APPS = "blocked_apps"

    private var sharedPreferences: SharedPreferences? = null
    private val editor = sharedPreferences?.edit()

    fun create(context: Context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun saveBlockedApps(apps: List<String>) {
        editor?.putStringSet(KEY_BLOCKED_APPS, apps.toSet())?.apply()
    }

    fun getBlockedApps(): List<String> {
        return sharedPreferences?.getStringSet(KEY_BLOCKED_APPS, emptySet())?.toList()
            ?: emptyList()
    }

    fun isAppBlocked(packageName: String): Boolean {
        return getBlockedApps().contains(packageName)
    }
}