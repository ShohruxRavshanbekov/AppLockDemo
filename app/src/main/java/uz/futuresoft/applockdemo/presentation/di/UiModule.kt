package uz.futuresoft.applockdemo.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.futuresoft.applockdemo.presentation.view_model.SharedViewModel

val uiModule = module {
    viewModel { SharedViewModel(sharedPreferences = get()) }
}