package com.allarma.hammington.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.Converters

@Database(entities = [AlarmProfile::class, Alarm::class], version = 2, exportSchema = false )
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AlarmProfileDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke( context: Context )= instance ?: synchronized(LOCK) {
            instance ?: buildDatabase( context ).also { instance = it }
        }

        private fun buildDatabase( context: Context ) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "allarma_data"
        ).fallbackToDestructiveMigration().build()
    }
}