package com.danielpasser.coronavirusinfo.ui.countrylistfragment

import androidx.lifecycle.*
import com.danielpasser.coronavirusinfo.model.CovidData
import com.danielpasser.coronavirusinfo.repository.RepositoryCountryList
import javax.inject.Inject
import com.danielpasser.coronavirusinfo.utils.DataState


class CountryListViewModel @Inject constructor(
    private val repository: RepositoryCountryList
) : ViewModel() {

    // val covidData: MediatorLiveData<Resource<CovidData>> = MediatorLiveData<Resource<CovidData>>()

    val covidData: LiveData<DataState<CovidData>> get() = repository.covidData
    //  private var _covidData = MutableLiveData<CovidData>()


    fun download() {
       repository.getCovidData()

    }

fun filter(country:String)
{
    repository.filterCountries(country)
}
}