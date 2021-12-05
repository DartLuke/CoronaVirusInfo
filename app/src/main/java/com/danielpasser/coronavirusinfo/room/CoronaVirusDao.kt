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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCountries(countries: List<Country>)

    @Query("SELECT * FROM CovidCountries WHERE Country LIKE :country")
    fun getFiltered(country:String): Single<List<Country>>

    @Query("SELECT * FROM CovidData")
    fun getAll(): Single<CovidData>

    @Query("SELECT * FROM CovidCountries")
    fun getAll1(): Single<List<Country>>



}