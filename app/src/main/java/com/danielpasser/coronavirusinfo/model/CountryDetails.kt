package com.danielpasser.coronavirusinfo.model

data class CountryDetails(val Country: String,
val Date:String,
val Confirmed: Int,
val Deaths:Int,
val Recovered: Int,
val Active: Int)