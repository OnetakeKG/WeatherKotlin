package net.nov.weatherkotlin.di

import net.nov.weatherkotlin.repository.Repository
import net.nov.weatherkotlin.repository.RepositoryImpl
import net.nov.weatherkotlin.ui.MainViewModel
import net.nov.weatherkotlin.ui.details.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.reflect.Array.get

val appModule = module {
    single<Repository> { RepositoryImpl() }

    //View models
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}