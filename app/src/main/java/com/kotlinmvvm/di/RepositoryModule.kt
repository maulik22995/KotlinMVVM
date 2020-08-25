package com.kotlinmvvm.di

import com.kotlinmvvm.ui.dashboard.news.LatestNewsRepo
import org.koin.dsl.module

val repositoryModule = module {
    factory {
        LatestNewsRepo()
    }
}