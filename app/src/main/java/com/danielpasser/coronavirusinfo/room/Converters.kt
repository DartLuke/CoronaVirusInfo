package com.danielpasser.coronavirusinfo.room

import android.provider.Settings
import androidx.room.TypeConverter
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.Global
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type;




object Converters {
    @JvmStatic
    @TypeConverter
    fun fromStringToGlobal(value: String?):Global?
    {
        val globalType: Type = object : TypeToken<Global>() {}.type
        return Gson().fromJson(value, globalType)
    }
    @JvmStatic
    @TypeConverter
    fun fromGlobal(global: Global): String? {
        val gson = Gson()
        return gson.toJson(global)
    }


    @JvmStatic
    @TypeConverter
    fun fromStringToArray(value: String?): List<Country?>? {
        val listType: Type = object : TypeToken<List<Country?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @JvmStatic
    @TypeConverter
    fun fromArrayList(list: List<Country>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}