package net.nov.weatherkotlin

import android.app.Application
import net.nov.weatherkotlin.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        org.koin.core.context.startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}