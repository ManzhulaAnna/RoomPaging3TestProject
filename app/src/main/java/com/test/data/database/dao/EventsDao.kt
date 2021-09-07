package com.test.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.test.data.model.EventsAndFav
import com.test.data.model.EventsDTO

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<EventsDTO>)

    @Query("DELETE FROM events_cache")
    suspend fun deleteData()

    @Query("SELECT * FROM events_cache ORDER BY event_date ASC")
    fun getEvents(): PagingSource<Int, EventsDTO>

    @Transaction
    @Query(
        """
        SELECT * FROM events_cache AS ec
        ORDER BY ec.event_date, ec.internal_id ASC
        """
    )
    fun getEventsAndFav(): PagingSource<Int, EventsAndFav>

    @Query("SELECT * FROM events_cache WHERE uid = :id")
    suspend fun getById(id: String): EventsDTO?
}