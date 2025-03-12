package uz.futuresoft.applockdemo

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.create(context = this)
    }
}