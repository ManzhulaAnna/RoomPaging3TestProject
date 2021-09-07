package com.test.api.helper

class MockNetworkFlavour : NetworkFlavourProvider {
    override val baseUrl: String
        get() = MOCK_URL

    override val timeOutInSeconds: Long
        get() = 60L

    override val okHttpVersion: String
        get() = MOCK_OKHTTP_VERSION

    companion object {
        const val MOCK_URL = "https://mock-geek.api.info/"
        const val MOCK_OKHTTP_VERSION = "1.1.1"
    }
}
