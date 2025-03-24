package uz.futuresoft.applockdemo.data

import android.content.Context

class SharedPreferencesManager(context: Context) {
    companion object {
        private const val NAME = "app_prefs"
        private const val KEY_PERMISSIONS = "permissions"
    }

    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setPermissionsState(permissionsAllowed: Boolean) {
        editor.putBoolean(KEY_PERMISSIONS, permissionsAllowed).apply()
    }

    fun arePermissionsAllowed(): Boolean {
        return sharedPreferences.getBoolean(KEY_PERMISSIONS, false)
    }
}