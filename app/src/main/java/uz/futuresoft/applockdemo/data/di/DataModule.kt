package uz.futuresoft.applockdemo.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.futuresoft.applockdemo.data.SharedPreferencesManager2
import uz.futuresoft.applockdemo.data.database.AppDatabase

val dataModule = module {
    single { AppDatabase.create(context = androidContext()) }
    single { SharedPreferencesManager2(context = androidContext()) }
}