package uz.futuresoft.applockdemo.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.futuresoft.applockdemo.presentation.activities.lock.LockViewModel
import uz.futuresoft.applockdemo.presentation.activities.main.MainViewModel

val uiModule = module {
    viewModel { MainViewModel(appRepository = get()) }
    viewModel { LockViewModel(database = get()) }
}