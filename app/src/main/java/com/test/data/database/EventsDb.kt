package com.test.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.data.database.converter.DateTypeConverters
import com.test.data.database.dao.EventsDao
import com.test.data.database.dao.EventsKeysDao
import com.test.data.database.dao.FavoritesDao
import com.test.data.model.EventsDTO
import com.test.data.model.EventsKeyDTO
import com.test.data.model.FavoriteEventsDTO

@Database(
    entities = [
        EventsDTO::class,
        FavoriteEventsDTO::class,
        EventsKeyDTO::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DateTypeConverters::class)
abstract class EventsDb : RoomDatabase() {
    abstract fun events(): EventsDao
    abstract fun favorites(): FavoritesDao
    abstract fun eventsKey(): EventsKeysDao

    companion object {
        fun create(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            EventsDb::class.java,
            DATA_BASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

        private const val DATA_BASE_NAME = "events.db"
    }
}