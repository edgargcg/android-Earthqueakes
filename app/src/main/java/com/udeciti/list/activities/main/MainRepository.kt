package com.udeciti.list.activities.main

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.udeciti.list.api.service
import com.udeciti.list.database.EarthquakeDatabase
import com.udeciti.list.models.Earthquake
import com.udeciti.list.models.EarthquakeJsonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val database: EarthquakeDatabase) {
    suspend fun fetchEarthquakes(sortBy: String): MutableList<Earthquake> {
        return withContext(Dispatchers.IO){
            val earthquakesResponse = service.getLastHourEarthquakes()
            val earthquakes = parseEarthquakeResult(earthquakesResponse)

            val sortByQuery = if(sortBy.isEmpty())  "time asc" else sortBy

            database.earthquakeDao.insertAll(earthquakes)
            database.earthquakeDao.getEarthquakes(SimpleSQLiteQuery("SELECT * FROM earthquakes order by $sortByQuery"))
        }
    }

    private fun parseEarthquakeResult(earthquakeJson: EarthquakeJsonResponse): MutableList<Earthquake> {
        val earthquakeList: MutableList<Earthquake> = mutableListOf<Earthquake>()
        val featureList = earthquakeJson.features

        for (feature in featureList){
            val properties = feature.properties
            val geometry = feature.geometry

            earthquakeList.add(
                Earthquake(
                    feature.id, properties.place, properties.mag, properties.time, geometry.longitude, geometry.latitude
                )
            )
        }

        return earthquakeList
    }
}