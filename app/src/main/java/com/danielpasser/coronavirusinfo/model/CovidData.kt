package com.danielpasser.coronavirusinfo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CovidData")
data class CovidData(
    @PrimaryKey
    val ID: String,
    val Message: String,
    val Global: Global,
    val Countries: List<Country>)



data class Global(
    val NewConfirmed: Long,
    val TotalConfirmed: Long,
    val NewDeaths: Long,
    val NewRecovered: Long,
    val TotalRecovered: Long,
    val Date: String
)
