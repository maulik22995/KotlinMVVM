package com.kotlinmvvm.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        androidContext().getSharedPreferences(androidContext().packageName,Context.MODE_PRIVATE)
    }
}