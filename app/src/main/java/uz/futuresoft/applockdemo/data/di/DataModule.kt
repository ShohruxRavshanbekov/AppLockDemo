package uz.futuresoft.applockdemo.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.futuresoft.applockdemo.data.SharedPreferencesManager
import uz.futuresoft.applockdemo.data.database.AppDatabase

val dataModule = module {
    single { AppDatabase.create(context = androidContext()) }
    single { SharedPreferencesManager(context = androidContext()) }
}