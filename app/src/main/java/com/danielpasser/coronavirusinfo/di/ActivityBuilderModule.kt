package com.danielpasser.coronavirusinfo.di

import com.danielpasser.coronavirusinfo.di.mainactivity.MainFragmentBuildersModule
import com.danielpasser.coronavirusinfo.di.mainactivity.MainViewModelsModule
import com.danielpasser.coronavirusinfo.ui.MainActivity

import dagger.Module


import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [MainFragmentBuildersModule::class, MainViewModelsModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity


}