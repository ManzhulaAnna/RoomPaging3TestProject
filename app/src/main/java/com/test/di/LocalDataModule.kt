package com.test.di

import android.app.Application
import com.test.data.database.EventsDb
import com.test.data.database.dao.EventsDao
import com.test.data.database.dao.EventsKeysDao
import com.test.data.database.dao.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun EventsDb(application: Application): EventsDb {
        return EventsDb.create(application)
    }

    @Provides
    @Singleton
    internal fun provideEventsDao(db: EventsDb): EventsDao {
        return db.events()
    }

    @Provides
    @Singleton
    internal fun provideFavoritesDao(db: EventsDb): FavoritesDao {
        return db.favorites()
    }

    @Provides
    @Singleton
    internal fun provideEventsKeysDao(db: EventsDb): EventsKeysDao {
        return db.eventsKey()
    }
}