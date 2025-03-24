package uz.futuresoft.applockdemo.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.futuresoft.applockdemo.data.SharedPreferencesManager
import uz.futuresoft.applockdemo.data.database.AppDatabase
import uz.futuresoft.applockdemo.data.repositories.AppRepository
import uz.futuresoft.applockdemo.data.repositories.AppRepositoryImpl
import uz.futuresoft.applockdemo.presentation.utils.DeviceAppsManager

val dataModule = module {
    single { AppDatabase.create(context = androidContext()) }
    single { get<AppDatabase>().appDao }
    single { SharedPreferencesManager(context = androidContext()) }
    single { DeviceAppsManager(context = androidContext()) }
    single<AppRepository> { AppRepositoryImpl(appDao = get(), deviceAppsManager = get()) }
}