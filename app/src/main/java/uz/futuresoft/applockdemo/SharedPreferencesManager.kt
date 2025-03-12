package uz.futuresoft.applockdemo

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesManager {
    private const val NAME = "app_prefs"
    private const val KEY_BLOCKED_APPS = "blocked_apps"

    private var sharedPreferences: SharedPreferences? = null
    private val editor = sharedPreferences?.edit()

    fun create(context: Context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun addToBlockedApps(packageName: String) {
        val existingBlockedAppsString = sharedPreferences?.getString(KEY_BLOCKED_APPS, "")
        val blockedApps =
            if (existingBlockedAppsString.isNullOrEmpty()) mutableListOf() else existingBlockedAppsString.toBlockedAppsList()
        blockedApps.add(packageName)
        val newBlockedAppsString = Gson().toJson(blockedApps)
        editor?.putString(KEY_BLOCKED_APPS, newBlockedAppsString)?.apply()
    }

    fun removeFromBlockedApps(packageName: String) {
        val existingBlockedAppsString = sharedPreferences?.getString(KEY_BLOCKED_APPS, "")
        val blockedApps =
            if (existingBlockedAppsString.isNullOrEmpty()) mutableListOf() else existingBlockedAppsString.toBlockedAppsList()
        blockedApps.remove(packageName)
        val newBlockedAppsString = Gson().toJson(blockedApps)
        editor?.putString(KEY_BLOCKED_APPS, newBlockedAppsString)?.apply()

    }

    fun getBlockedApps(): List<String> {
        val existingBlockedAppsString = sharedPreferences?.getString(KEY_BLOCKED_APPS, "")
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(existingBlockedAppsString, type)
    }

    private fun String.toBlockedAppsList(): MutableList<String> {
        val type = object : TypeToken<MutableList<String>>() {}.type
        return Gson().fromJson(this, type)
    }
}