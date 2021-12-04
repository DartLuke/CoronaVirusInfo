package com.danielpasser.coronavirusinfo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CovidData

@Database(entities = [CovidData::class],version = 1)
@TypeConverters(Converters::class)
abstract class CoronaVirusDB : RoomDatabase() {
    abstract fun CoronaVirusDao(): CoronaVirusDao

    companion object {
        val DATABASE_NAME: String = "corona_virus_dao"
    }
}