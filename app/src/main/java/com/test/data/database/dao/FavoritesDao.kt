package com.test.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.data.model.FavoriteEventsDTO

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<FavoriteEventsDTO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: FavoriteEventsDTO)

    @Query("DELETE FROM events_favorite")
    suspend fun deleteData()

    @Query("DELETE FROM events_favorite WHERE uid=:id")
    suspend fun deleteById(id: String)

    @Query("SELECT EXISTS (SELECT 1 FROM events_favorite WHERE uid=:id)")
    suspend fun isFavorite(id: String): Int

    @Query("SELECT * FROM events_favorite WHERE uid=:id")
    suspend fun getById(id: String): FavoriteEventsDTO?

    @Query("SELECT * FROM events_favorite WHERE uid IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<FavoriteEventsDTO>?

    @Query("SELECT * FROM events_favorite ORDER BY date ASC")
    fun getPagedSource(): PagingSource<Int, FavoriteEventsDTO>
}
