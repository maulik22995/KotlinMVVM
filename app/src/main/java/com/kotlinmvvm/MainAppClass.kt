package com.kotlinmvvm

import android.app.Application
import com.kotlinmvvm.di.appModule
import com.kotlinmvvm.di.networkModule
import com.kotlinmvvm.di.repositoryModule
import com.kotlinmvvm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainAppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoine()
    }

    private fun initKoine() {
        startKoin {
            androidContext(this@MainAppClass)
            modules(listOf(appModule, networkModule, repositoryModule, viewModelModule))
        }
    }
}