package com.kotlinmvvm.model


import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(SourcesConverter::class)
data class Source(
    @SerializedName("id")
    var id: Any?,
    @SerializedName("name")
    var name: String?
)