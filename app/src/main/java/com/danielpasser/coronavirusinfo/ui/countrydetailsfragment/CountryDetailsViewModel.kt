package com.danielpasser.coronavirusinfo.ui.countrydetailsfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.danielpasser.coronavirusinfo.model.CountryDetails
import com.danielpasser.coronavirusinfo.repository.RepositoryCountryDetails
import com.danielpasser.coronavirusinfo.utils.DataState
import javax.inject.Inject

class CountryDetailsViewModel @Inject constructor(
    private val repository: RepositoryCountryDetails
) : ViewModel() {
    val covidData: LiveData<DataState<List<CountryDetails>>> get() = repository.covidData

    fun getCovidData(country:String)
    {
        repository.getCovidData(country)
    }

}
