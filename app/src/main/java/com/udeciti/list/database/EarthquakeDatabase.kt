package com.udeciti.list.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.udeciti.list.models.Earthquake

@Database(entities =  [Earthquake::class], version = 1)
abstract class EarthquakeDatabase : RoomDatabase(){
    abstract val earthquakeDao: EarthqueakeDAO
}

private lateinit var  INSTANCE: EarthquakeDatabase

fun getDatabase(context: Context): EarthquakeDatabase{
    synchronized(EarthquakeDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE = databaseBuilder(
                context.applicationContext,
                EarthquakeDatabase::class.java,
                "earthquake_db"
            ).build()
        }

        return INSTANCE
    }
}
