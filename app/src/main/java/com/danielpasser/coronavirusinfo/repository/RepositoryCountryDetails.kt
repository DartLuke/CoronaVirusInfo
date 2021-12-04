package com.danielpasser.coronavirusinfo.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danielpasser.coronavirusinfo.model.CountryDetails

import com.danielpasser.coronavirusinfo.retrofit.Api
import com.danielpasser.coronavirusinfo.utils.DataState
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryCountryDetails @Inject constructor(

    private val api: Api
) {
    val covidData: LiveData<DataState<List<CountryDetails>>>  get() = _covidData
    private var _covidData = MutableLiveData<DataState<List<CountryDetails>>>()
    @SuppressLint("CheckResult")
    fun getCovidData(country:String)
    {

        val formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val from: String = LocalDate.now().format(formatter)
        val to:String=LocalDate.now().minusWeeks(2).format(formatter)
        api.groupList(country,from,to)
            .toObservable().subscribeOn(Schedulers.io()).
                subscribeWith(object :DisposableObserver<List<CountryDetails>>(){
                    override fun onNext(t: List<CountryDetails>) {
                     _covidData.postValue(DataState.Success(t))

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