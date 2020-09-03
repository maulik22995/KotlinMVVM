package com.kotlinmvvm.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "newslocality")
data class NewsLocality(
    @PrimaryKey
    @SerializedName("countryCode")
    var countryCode: String,
    @SerializedName("countryName")
    var countryName: String?
)