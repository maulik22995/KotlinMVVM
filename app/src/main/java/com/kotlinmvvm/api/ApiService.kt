package com.kotlinmvvm.api

import ke.co.ipandasoft.newsfeed.data.remote.responses.NewsResponse
import ke.co.ipandasoft.newsfeed.models.NewsLocality
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("/v2/top-headlines")
    suspend fun loadHeadlines(@Query("country") countryCode:String): Response<NewsResponse>

    @GET("/v2/top-headlines")
    suspend fun loadCategoryHeadlines(@Query("country") countryCode:String, @Query("category") category:String): Response<NewsResponse>

    @GET("/v2/sources")
    suspend fun getSourceBased(): Response<NewsResponse>

    @GET
    suspend fun getNewsApiCountries(@Url url:String): Response<List<NewsLocality>>
}