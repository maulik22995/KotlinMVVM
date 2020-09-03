package com.kotlinmvvm.utils.extension

import androidx.lifecycle.MutableLiveData

fun<T> MutableLiveData<T>.initWith(data : T) : MutableLiveData<T> = this.apply {
    this.value = data
}