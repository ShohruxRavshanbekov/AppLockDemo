package uz.futuresoft.applockdemo

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.accessibility.AccessibilityEvent

class AppBlockerService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            if (isAppBlocked(packageName = packageName)) {
                openBlockScreen()
            }
        }
    }

    override fun onInterrupt() {}

    private fun isAppBlocked(packageName: String): Boolean {
        val blockedApps = SharedPreferencesManager.getBlockedApps()
        return packageName in blockedApps
    }

    private fun openBlockScreen() {
        val intent = Intent(this, BlockScreenActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}