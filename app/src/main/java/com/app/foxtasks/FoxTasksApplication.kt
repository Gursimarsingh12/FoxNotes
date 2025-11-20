package com.app.foxtasks

import android.app.Application
import com.app.foxtasks.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FoxTasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@FoxTasksApplication)
            modules(appModule)
        }
    }
}
