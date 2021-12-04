package com.danielpasser.coronavirusinfo.di

import android.app.Application
import androidx.annotation.Nullable
import androidx.room.Room
import com.danielpasser.coronavirusinfo.room.CoronaVirusDB
import com.danielpasser.coronavirusinfo.room.CoronaVirusDao
import com.danielpasser.coronavirusinfo.retrofit.Api
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = [ViewModelFactoryModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideGithubService(): Api {
        return Retrofit.Builder()
            .baseUrl("https://api.covid19api.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): CoronaVirusDB {
        return Room
            .databaseBuilder(app, CoronaVirusDB::class.java, CoronaVirusDB.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideCountryDao(db: CoronaVirusDB): CoronaVirusDao {
        return db.CoronaVirusDao()
    }

}