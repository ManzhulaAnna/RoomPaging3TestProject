package com.test.api.helper

interface NetworkFlavourProvider {
    val baseUrl: String
    val timeOutInSeconds: Long
    val okHttpVersion: String
}
