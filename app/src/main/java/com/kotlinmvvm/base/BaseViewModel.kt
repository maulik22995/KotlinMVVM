package com.kotlinmvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinmvvm.api.ApiService
import kotlinx.coroutines.cancel
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel : ViewModel(),KoinComponent {

    protected val apiService: ApiService by inject()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}