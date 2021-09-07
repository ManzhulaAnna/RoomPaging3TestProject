package com.test.api

import retrofit2.mock.BehaviorDelegate
import com.google.gson.Gson
import com.test.api.data.EventsModelFactory
import com.test.api.helper.RetrofitDelegateHelper
import com.test.api.helper.TestHelper
import com.test.api.helper.NetworkFlavourProvider
import com.test.data.model.EventsRaw
import com.test.data.network.EventsService
import retrofit2.Response

/**
 * All non-primitive arguments of the overriden methods must be optional. This is necessary to
 * ensure that Mockito argument matchers continue to work. As we move to MockK they can be
 * converted to non-optionals.
 *
 * @Mockable
 */

@Suppress("DeferredIsResult")
open class MockApi constructor(
    private val gson: Gson,
    networkFlavourProvider: NetworkFlavourProvider
) : EventsService {
    private val delegate: BehaviorDelegate<EventsService>
    private val retrofitDelegateHelper = RetrofitDelegateHelper()

    init {
        TestHelper.classLoader = this.javaClass.classLoader
        delegate = retrofitDelegateHelper.createBehaviorDelegate(networkFlavourProvider.baseUrl)
    }

    override suspend fun getEvents(
        clientId: String,
        page: Int,
        pageSize: Int,
        sort: String
    ): Response<EventsRaw> {
        val contentJson = EventsModelFactory.generateJsonOfEvents()
        val result = gson.fromJson(contentJson, EventsRaw::class.java)
        return delegate.returningResponse(result).getEvents(clientId, page, pageSize, sort)
    }
}
