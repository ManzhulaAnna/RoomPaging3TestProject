package com.test.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.data.model.EventsKeyDTO

@Dao
interface EventsKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<EventsKeyDTO>)

    @Query("DELETE FROM events_keys")
    suspend fun deleteAll()

    @Query("SELECT * FROM events_keys WHERE uid = :id")
    suspend fun remoteKeyById(id: String): EventsKeyDTO?
}