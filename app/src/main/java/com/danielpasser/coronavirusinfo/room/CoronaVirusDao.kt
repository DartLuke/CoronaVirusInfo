package com.danielpasser.coronavirusinfo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CovidData
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CoronaVirusDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(covidData: CovidData) : Long

    @Query("SELECT * FROM CovidData")
    fun getAll(): Single<CovidData>

}