package com.danielpasser.coronavirusinfo.di.mainActivity

import androidx.lifecycle.ViewModel
import com.danielpasser.coronavirusinfo.di.ViewModelKey
import com.danielpasser.coronavirusinfo.ui.countrydetailsfragment.CountryDetailsViewModel
import com.danielpasser.coronavirusinfo.ui.countrylistfragment.CountryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(CountryListViewModel ::class)
    abstract fun bindsCountryListViewModel(viewModel: CountryListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountryDetailsViewModel ::class)
    abstract fun bindsCountryDetailsViewModel(viewModel: CountryDetailsViewModel) : ViewModel
}