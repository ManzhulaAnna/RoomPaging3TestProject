package com.test.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppDispatchers(
    private val default: CoroutineDispatcher,
    private val io: CoroutineDispatcher,
    private val main: CoroutineDispatcher,
) : DispatchersProvider {
    companion object {
        val Default = AppDispatchers(
            default = Dispatchers.Default,
            io = Dispatchers.IO,
            main = Dispatchers.Main.immediate,
        )
    }

    override fun main(): CoroutineDispatcher {
        return main
    }

    override fun default(): CoroutineDispatcher {
        return default
    }

    override fun io(): CoroutineDispatcher {
        return io
    }

    override fun unconfined(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}