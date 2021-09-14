package com.udeciti.list.activities.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udeciti.list.api.Response
import com.udeciti.list.database.getDatabase
import com.udeciti.list.models.Earthquake
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.UnknownHostException

class MainViewModel(application: Application, sortBy: String): AndroidViewModel(application) {
    private val database = getDatabase(application.applicationContext)
    private val repository = MainRepository(database)
    private val earthquakeSortBy = sortBy

    private val _status = MutableLiveData<Response>()
    val status: LiveData<Response>
        get() = _status

    private var _earthquakeList = MutableLiveData<MutableList<Earthquake>>()
    val earthquakesList: LiveData<MutableList<Earthquake>>
        get() = _earthquakeList

    init {
        loadEarthquakes()
    }

    public fun loadEarthquakes(sortBy: String = earthquakeSortBy) {
        viewModelScope.launch {
            try {
                _status.value = Response.LOADING
                _earthquakeList.value = repository.fetchEarthquakes(sortBy)
                _status.value = Response.DONE
            } catch (e: UnknownHostException) {
                _status.value = Response.NOT_INTERNET_CONNECTION
            } catch (e: Exception) {
                _status.value = Response.ERROR
            }
        }
    }
}