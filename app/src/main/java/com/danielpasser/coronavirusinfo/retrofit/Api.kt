package com.danielpasser.coronavirusinfo.retrofit

import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CountryDetails
import com.danielpasser.coronavirusinfo.model.CovidData
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET(value = "summary")
   fun getAllData(

   ) : Single<CovidData>

    @GET("country/{country}")
    fun groupList(@Path("country") country: String,
                  @Query("from") from:String,
                  @Query("to")  to:String
                     ): Flowable<List<CountryDetails>>

}