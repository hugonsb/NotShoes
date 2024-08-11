package com.ahpp.notshoes

import android.app.Application
import com.ahpp.notshoes.di.appModule
import com.ahpp.notshoes.di.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class NotShoesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            //androidLogger(level = Level.DEBUG) //ver logs do koin
            androidContext(this@NotShoesApplication) //contexto para o ThemeViewModel
            modules(appModule, dbModule)
        }
    }
}