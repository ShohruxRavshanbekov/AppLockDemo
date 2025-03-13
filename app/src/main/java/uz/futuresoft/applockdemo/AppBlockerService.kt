package uz.futuresoft.applockdemo

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.futuresoft.applockdemo.utils.SharedPreferencesManager

class AppBlockerService : Service() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var blockedApps: List<String>
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        sharedPreferencesManager = SharedPreferencesManager(context = this)
        blockedApps = sharedPreferencesManager.getBlockedApps()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        startAppMonitoring()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startAppMonitoring() {
        CoroutineScope(Dispatchers.IO).launch {
            while (isRunning) {
                val foregroundApp = getForegroundApp(this@AppBlockerService)
                if (blockedApps.contains(foregroundApp)) {
                    blockApp(packageName = foregroundApp)
                }
                delay(1000)
            }
        }
    }

    private fun getForegroundApp(context: Context): String? {
        val usageManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val usageEvents = usageManager.queryEvents(time - 5000, time)
        val event = UsageEvents.Event()
        var lastApp: String? = null

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                lastApp = event.packageName
            }
        }
        return lastApp
    }

    // This method shows the lock screen on top of the blocked app
    private fun blockApp(packageName: String?) {
        val intent = Intent(this, BlockScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("packageName", packageName)
        startActivity(intent)
    }

    // This method immediately closes the blocked app
    private fun blockApp() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
    }

    private fun startForegroundService() {
        val channelId = "app_block_channel"
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Blocked apps")
            .setContentText(blockedApps.joinToString(separator = ",\n"))
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
}