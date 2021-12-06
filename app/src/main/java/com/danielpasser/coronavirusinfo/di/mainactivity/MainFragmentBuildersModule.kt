package com.danielpasser.coronavirusinfo.di.mainactivity

import com.danielpasser.coronavirusinfo.ui.countrydetailsfragment.CountryDetailsFragment
import com.danielpasser.coronavirusinfo.ui.countrylistfragment.CountryListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun countryListFragment():CountryListFragment

    @ContributesAndroidInjector
    abstract fun countrDetailsFragment():CountryDetailsFragment
}