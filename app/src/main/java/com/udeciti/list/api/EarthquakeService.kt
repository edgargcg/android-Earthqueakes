package com.udeciti.list.api

import com.udeciti.list.models.EarthquakeJsonResponse
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface EarthquakeService {
    @GET("all_hour.geojson")
    suspend fun getLastHourEarthquakes(): EarthquakeJsonResponse
}

private var retrofit = Retrofit.Builder()
    .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

var service: EarthquakeService = retrofit.create(EarthquakeService::class.java)