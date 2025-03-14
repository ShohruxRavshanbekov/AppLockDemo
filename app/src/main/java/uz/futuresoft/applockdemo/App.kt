package uz.futuresoft.applockdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import uz.futuresoft.applockdemo.utils.SharedPreferencesManager2

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager2.create(context = this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelId = "app_block_channel"
        val channelName = "App Blocker Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW,
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}