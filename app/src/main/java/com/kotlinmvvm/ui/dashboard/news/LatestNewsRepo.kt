package com.kotlinmvvm.ui.dashboard.news

import android.util.Log
import com.google.gson.Gson
import com.kotlinmvvm.base.BaseRepository
import ke.co.ipandasoft.newsfeed.data.remote.responses.NewsResponse
import ke.co.ipandasoft.newsfeed.data.remote.responses.ResultWrapper
import java.lang.Exception

class LatestNewsRepo : BaseRepository() {

    suspend fun getNewsHeadLines(countryCode: String): ResultWrapper<NewsResponse> {
        return try {
            val dataResponse=apiService.loadHeadlines(countryCode)
            var newsResponse: NewsResponse?=null
            when {
                dataResponse.isSuccessful -> {
                    Log.e(">>","SUCCESS RESP ${Gson().toJson(dataResponse.body())}")
                    newsResponse= dataResponse.body()!!
                }
                else -> {
                    Log.e(">>","ERROR RESP ${Gson().toJson(dataResponse.errorBody())}")
                }
            }
            return ResultWrapper.Success(newsResponse!!)
        }catch (exception: Exception){
            val errorResponse= ResultWrapper.Error(exception)
            Log.e(">>","ERROR RESP ${Gson().toJson(errorResponse.exception.localizedMessage)}")
            return ResultWrapper.Error(errorResponse.exception)
        }
    }
}