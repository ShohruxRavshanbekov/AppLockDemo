package uz.futuresoft.applockdemo.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.futuresoft.applockdemo.presentation.activities.lock.LockViewModel
import uz.futuresoft.applockdemo.presentation.activities.main.MainViewModel
import uz.futuresoft.applockdemo.presentation.screens.apps.AppsViewModel
import uz.futuresoft.applockdemo.presentation.screens.permissions.PermissionsViewModel

val uiModule = module {
    viewModel { MainViewModel(sharedPreferencesManager = get()) }
    viewModel { LockViewModel(database = get()) }
    viewModel { PermissionsViewModel(sharedPreferencesManager = get()) }
    viewModel { AppsViewModel(appRepository = get()) }
}