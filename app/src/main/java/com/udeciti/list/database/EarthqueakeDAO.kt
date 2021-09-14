package com.udeciti.list.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.udeciti.list.models.Earthquake

@Dao
interface EarthqueakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(earthquakeList: MutableList<Earthquake>)

    @RawQuery
    fun getEarthquakes(query: SupportSQLiteQuery): MutableList<Earthquake>

    @Query("SELECT * FROM earthquakes WHERE magnitude > :magnitude")
    fun getEarthquakesWithMagnitude(magnitude: Double): MutableList<Earthquake>

    @Update
    fun update(vararg eqs: Earthquake)

    @Delete
    fun delete(vararg eqs: Earthquake)
}