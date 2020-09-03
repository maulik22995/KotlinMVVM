package com.kotlinmvvm.ui.dashboard.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kotlinmvvm.base.BaseViewModel
import com.kotlinmvvm.model.ResultWrapper
import com.kotlinmvvm.model.Article
import com.kotlinmvvm.utils.extension.initWith
import kotlinx.coroutines.launch

class LatestNewsViewModel(private val repository: LatestNewsRepo) : BaseViewModel() {

    private val recentNewsMutableData: MutableLiveData<ArrayList<Article>> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>().initWith(false)
    val isSwipeRefLoading = MutableLiveData<Boolean>().initWith(false)
    val isLast = MutableLiveData<Boolean>().initWith(false)
    val recentNewsLiveData: LiveData<ArrayList<Article>>
        get() = recentNewsMutableData
    var pageNo: Int = 1

    fun getLocalizedNews(countryCode: String) {
        viewModelScope.launch {
            isSwipeRefLoading.value?.let {
                if (pageNo == 1 && !it) {
                    isLoading.value = true
                }
            }
            when (val resultResponse = repository.getNewsHeadLines(countryCode, pageNo)) {
                is ResultWrapper.Success -> {
                    val latestNews = resultResponse.data
                    recentNewsMutableData.postValue(latestNews.articles)
                    pageNo++
                    val totalPage = ((latestNews.totalResults)?.div(20) ?: 5) + 2
                    isLoading.value = false
                    isLast.value = pageNo == totalPage
                }
                is ResultWrapper.Loading -> {
                    isLoading.value = true
                    isSwipeRefLoading.value = false
                }
                is ResultWrapper.Error -> {
                    Log.d(">>", "Error in response ${resultResponse.exception.localizedMessage}")
                    isLoading.value = false
                    isSwipeRefLoading.value = false
                }
            }
        }
    }
}