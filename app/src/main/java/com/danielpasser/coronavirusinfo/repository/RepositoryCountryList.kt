package com.danielpasser.coronavirusinfo.repository


import android.annotation.SuppressLint
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danielpasser.coronavirusinfo.model.Country
import com.danielpasser.coronavirusinfo.model.CountryDetails
import com.danielpasser.coronavirusinfo.model.CovidData
import com.danielpasser.coronavirusinfo.retrofit.Api
import com.danielpasser.coronavirusinfo.room.CoronaVirusDao
import com.danielpasser.coronavirusinfo.utils.DataState
import com.danielpasser.coronavirusinfo.utils.NetworkBoundResource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class RepositoryCountryList
@Inject constructor(
    private val repoDao: CoronaVirusDao,
    private val api: Api
) {


    val covidData: LiveData<DataState<CovidData>> get() = _covidData
    private var _covidData = MutableLiveData<DataState<CovidData>>()

    private val countryList=object : NetworkBoundResource<CovidData, CovidData>()
    {
        override fun processResult(it: CovidData): CovidData {
         _covidData.postValue(DataState.Success(it))
         repoDao.insertAllCountries(it.Countries)
           return it
        }

        override fun saveCallResult(item: CovidData) {
            repoDao.insertAll(item)
        }

        override fun loadFromDb(): Single<CovidData> {
          return  repoDao.getAll()
        }

        override fun createCall(): Single<CovidData> {
            return api.getAllData()
        }

        override fun fetchOnError(error: Throwable): Boolean {

          Log.e("Test","Exeption"+error.message.toString())
            return super.fetchOnError(error)
        }
    }
    fun getCovidData(){
        _covidData.value=DataState.Loading
        countryList.clear()
        countryList.asObservable().subscribe()


    }
    @SuppressLint("CheckResult")
    fun filterCountries(country:String)
    {

        repoDao.getFiltered("%$country%")
            .toObservable().subscribeOn(Schedulers.io()).
            subscribeWith(object : DisposableObserver<List<Country>>(){
                override fun onNext(countries: List<Country>) {
                    _covidData.postValue(DataState.Success(CovidData("","",countries)))

                }

                override fun onError(e: Throwable) {
                    _covidData.postValue(DataState.Error(e))
                }

                override fun onComplete() {

                }

                @SuppressLint("CheckResult")
                override fun onStart() {
                    _covidData.value=DataState.Loading
                }
            })
    }
}