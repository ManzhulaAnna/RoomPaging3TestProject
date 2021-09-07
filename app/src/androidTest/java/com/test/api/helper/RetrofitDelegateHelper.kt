package com.test.api.helper

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.data.network.EventsService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

class RetrofitDelegateHelper {

    fun createResponseBody(contentJson: String?): ResponseBody? {
        return contentJson?.toResponseBody("application/json".toMediaTypeOrNull())
    }

    fun createBehaviorDelegate(baseUrl: String): BehaviorDelegate<EventsService> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(createCustomGson()))
            .baseUrl(baseUrl)
            .build()
        val mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(createNetworkBehaviorDelegate())
            .build()
        return mockRetrofit.create(EventsService::class.java)
    }

    private fun createCustomGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    private fun createNetworkBehaviorDelegate(): NetworkBehavior {
        val networkBehavior = NetworkBehavior.create()
        networkBehavior.setFailurePercent(0)
        networkBehavior.setDelay(0, TimeUnit.MILLISECONDS)
        return networkBehavior
    }
}