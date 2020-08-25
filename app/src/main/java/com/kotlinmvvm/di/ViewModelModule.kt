package com.kotlinmvvm.di

import com.kotlinmvvm.ui.dashboard.HomeViewModel
import com.kotlinmvvm.ui.dashboard.news.LatestNewsViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        HomeViewModel()
    }

    factory {
        LatestNewsViewModel(get())
    }
}