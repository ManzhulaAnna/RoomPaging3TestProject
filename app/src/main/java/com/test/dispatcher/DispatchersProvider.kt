package com.test.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}