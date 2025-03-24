package uz.futuresoft.applockdemo.presentation

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
import org.koin.android.ext.android.inject
import uz.futuresoft.applockdemo.R
import uz.futuresoft.applockdemo.data.database.AppDao
import uz.futuresoft.applockdemo.presentation.activities.lock.LockActivity

class AppBlockerService : Service() {
    private val appDao: AppDao by inject()
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        startAppMonitoring()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    private fun startAppMonitoring() {
        CoroutineScope(Dispatchers.IO).launch {
            while (isRunning) {
                val blockedApps = appDao.getApps()
                    .filter { it.locked }
                    .map { it.packageName }
                val launchedApp = getLaunchedApp(this@AppBlockerService)
                if (blockedApps.contains(launchedApp)) {
                    showLockScreen(packageName = launchedApp)
                }
                delay(1000)
            }
        }
    }

    private fun getLaunchedApp(context: Context): String? {
        val usageManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val usageEvents = usageManager.queryEvents(time - 5000, time)
        val event = UsageEvents.Event()
        var launchedApp: String? = null

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                launchedApp = event.packageName
            }
        }
        return launchedApp
    }

    // This method shows the lock screen on top of the blocked app
    private fun showLockScreen(packageName: String?) {
        val intent = Intent(this, LockActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        intent.putExtra("packageName", packageName)
        startActivity(intent)
    }

    // This method immediately closes the blocked app
    private fun closeApp() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
    }

    private fun startForegroundService() {
        val channelId = "app_block_channel"
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("App blocker is running")
            .setContentText("Some apps are blocked")
            .build()

        startForeground(1, notification)
    }
}