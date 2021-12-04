package com.danielpasser.coronavirusinfo.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danielpasser.coronavirusinfo.model.CovidData
import com.danielpasser.coronavirusinfo.retrofit.Api
import com.danielpasser.coronavirusinfo.room.CoronaVirusDao
import com.danielpasser.coronavirusinfo.utils.DataState
import com.danielpasser.coronavirusinfo.utils.NetworkBoundResource
import io.reactivex.Single
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
    fun getCovidData()=countryList.asObservable()
}