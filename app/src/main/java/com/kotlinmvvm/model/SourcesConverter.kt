package com.kotlinmvvm.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotlinmvvm.model.Source
import java.lang.reflect.Type

class SourcesConverter{

    val gson = Gson()

    val type: Type = object : TypeToken<Source?>() {}.type


    @TypeConverter
    fun fromSource(source: Source?): String{
        return gson.toJson(source,type)
    }

    @TypeConverter
    fun toSource(json: String?): Source {
        return gson.fromJson(json,type)
    }
}