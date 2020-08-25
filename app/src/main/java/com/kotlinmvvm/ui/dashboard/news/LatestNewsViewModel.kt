package com.kotlinmvvm.ui.dashboard.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kotlinmvvm.base.BaseViewModel
import ke.co.ipandasoft.newsfeed.data.remote.responses.ResultWrapper
import ke.co.ipandasoft.newsfeed.models.Article
import kotlinx.coroutines.launch

class LatestNewsViewModel(private val repository : LatestNewsRepo) : BaseViewModel() {

    private val recentNewsMutableData: MutableLiveData<List<Article>> = MutableLiveData()
    val recentNewsLiveData: LiveData<List<Article>>
        get() = recentNewsMutableData

    fun getLocalizedNews(countryCode: String){
        viewModelScope.launch {
            when(val resultResponse =repository.getNewsHeadLines(countryCode)) {
                is ResultWrapper.Success ->{
                    val latestNews=resultResponse.data
                    recentNewsMutableData.postValue(latestNews.articles)
                }
                is ResultWrapper.Error->{
                    Log.d(">>","Error in response ${resultResponse.exception.localizedMessage}")
                }
            }
        }
    }
}