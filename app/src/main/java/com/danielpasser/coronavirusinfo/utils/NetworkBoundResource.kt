package com.danielpasser.coronavirusinfo.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringDef
import androidx.room.EmptyResultSetException
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

abstract class NetworkBoundResource <ResultType, RequestType> {
    /**
     * A easy way to get the current state of data
     */
    private val value: ResultType?
        get() = result.value

    /**
     * Can be changed to return a Resource class that encapsulate both the data and its state as well.
     */
    private val result = BehaviorSubject.create<ResultType>()

    /**
     * Helps in resetting the output or result, by filtering out last value when {@code State} is OFF
     */
    @State
    private var state = OFF

    /**
     * A simple solution for when the observable would be subscribed in more than one places, since fetching more than once isn't really necessary unless it meets {@code shouldFetch} condition.
     */
    private var firstCallFinished: Boolean = false

    /**
     * A simple helper to debounce initial subscription if the data is subscribed to more than one streams at the same time.
     */
    private val debouncer = Debouncer()



    /**
     * Fetch the data from network and persist into DB, make required transformations and then send it back to UI. This function can also be used to request a new data from the server.
     */
    private fun fetchFromNetwork(): Single<ResultType> {
        return createCall()
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                saveCallResult(it)
            }
            .map { processResult(it) }
            .doOnSuccess {
                firstCallFinished = true
                setValue(it)
            }
    }

    /**
     * Send the data back to UI.
     */
    private fun setValue(newValue: ResultType) {
        if (result.value != newValue) {
            result.onNext(newValue)
        }
    }

    /**
     * A method exposed send a new stream of data to the UI as well as to persist into DB. The callback returns the last state as argument.
     */
    fun setValueCallback(callback: (ResultType?) -> RequestType) {
        val item = callback(value)
        val newValue = processResult(item)
        saveCallResult(item)
        setValue(newValue)
    }

    abstract fun processResult(it: RequestType): ResultType

    /**
     * Called to save the result of the API response into the database
     */
    protected abstract fun saveCallResult(item: RequestType)

    /**
     * Called with the data in the database to decide whether to fetch potentially updated data from the network.
     */
    open fun shouldFetch(data: ResultType?): Boolean {
        return data == null
    }

    /**
     * Called to get the cached data from the database.
     */
    protected abstract fun loadFromDb(): Single<RequestType>

    /**
     * Called to create the API call.
     */
    protected abstract fun createCall(): Single<RequestType>

    /**
     * Called when the fetch fails
     */
    protected open fun onFetchFailed(error: Throwable) {
    }

    /**
     * Called when the first value is returned to the UI.
     */
    protected open fun onFirstSet(newValue: ResultType) {
    }

    /**
     * Handle all the exceptions what would require you to fetch the data from the server. Room returns {@link EmptyResultSetException} when the record isn't found
     */
    protected open fun fetchOnError(error: Throwable) = error is EmptyResultSetException

    /**
     * Returns a Observable object that represents the resource that's implemented in the base class.
     * @param force Call with true to force running the code through {@code shouldFetch} condition.
     */
    fun asObservable(force: Boolean = false): Observable<ResultType> = result
        .doOnSubscribe {
            if (firstCallFinished && !force) return@doOnSubscribe
            getInitialData()
        }.filter { state == ON }

    /**
     * Get the initial data, and debouce this method call to avoid fetching on two simultaneous subscriptions.
     */
    @SuppressLint("CheckResult")
    private fun getInitialData() {
        debouncer.debounce(Void::class.java, Runnable {
            asSingle()
                .subscribe({
                }, {
                    onFetchFailed(it)
                })
        }, 300, TimeUnit.MILLISECONDS)
    }


    /**
     * Returns a Single object that can be used to get the required data depending on {@code shouldFetch} condition. To be used when the
     */
    fun asSingle(): Single<ResultType> = loadFromDb()
        .subscribeOn(Schedulers.io())
        .map {
            processResult(it)
        }
        .doOnSubscribe {
            state = ON
        }
        // Watch for a particular error, that can be used to trigger the network fetch.
        .onErrorResumeNext { error ->
            if (fetchOnError(error)) {
                return@onErrorResumeNext fetchFromNetwork()
            }
            throw error
        }
        // Switch case depending on whether we should fetch new data or not.
        .flatMap {
            setValue(it)
            return@flatMap if (shouldFetch(it) && !firstCallFinished) fetchFromNetwork()
            else Single.just(it)
        }.doOnSuccess {
            setValue(it)
            onFirstSet(it)
            firstCallFinished = true
        }
        .doOnError {
            onFetchFailed(it)
        }

    /**
     * Clear the state of the database, so that it re-fetches on subscribe. Used for when the data source has to be flushed.
     */
    fun clear() {
        firstCallFinished = false
        state = OFF
    }

    companion object {
        @StringDef(ON, OFF)
        private annotation class State

        private const val OFF = "off"
        private const val ON = "on"
    }
}