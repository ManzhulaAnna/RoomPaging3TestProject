package com.test.data.network

import androidx.annotation.IntRange
import androidx.annotation.WorkerThread
import com.test.data.model.EventsRaw
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsService {
    @WorkerThread
    @GET("/$API_VERSION/events")
    suspend fun getEvents(
        @Query("client_id") clientId: String,
        @Query("page") @IntRange(from = 1) page: Int = INITIAL_PAGE,
        @Query("per_page") @IntRange(from = 1, to = MAX_SIZE) pageSize: Int = DEFAULT_SIZE,
        @Query("sort") sort: String = DEFAULT_SORT,
    ): Response<EventsRaw>

    companion object {
        const val DEFAULT_SIZE = 30
        const val MAX_SIZE = 50L
        const val INITIAL_PAGE = 1
        const val DEFAULT_SORT = "datetime_utc.asc"

        private const val API_VERSION = 2
    }
}