package com.kotlinmvvm.base

import com.kotlinmvvm.api.ApiService
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseRepository : KoinComponent {
    val apiService : ApiService by inject()
}