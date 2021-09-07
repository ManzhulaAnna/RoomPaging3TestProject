package com.test.dispatchers

import com.test.dispatcher.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Unconfined

class MockDispatchers : DispatchersProvider {
    override fun main(): CoroutineDispatcher = Unconfined
    override fun default(): CoroutineDispatcher = Unconfined
    override fun io(): CoroutineDispatcher = Unconfined
    override fun unconfined(): CoroutineDispatcher = Unconfined
}
