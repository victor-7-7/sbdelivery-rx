package ru.skillbranch.sbdelivery

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.skillbranch.sbdelivery.di.AppModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
//            androidLogger() // Level.INFO <- имеет баг
            // https://github.com/InsertKoinIO/koin/issues/1076
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    AppModule.appModule(),
                    AppModule.databaseModule(),
                    AppModule.viewModelModule(),

                    )
            )
        }
    }

}