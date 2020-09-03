package com.kotlinmvvm.model


import com.google.gson.annotations.SerializedName
import com.kotlinmvvm.model.Article

data class NewsResponse(
    @SerializedName("articles")
    var articles: ArrayList<Article>?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("totalResults")
    var totalResults: Int?
)