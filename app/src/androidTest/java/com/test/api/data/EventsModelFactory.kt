package com.test.api.data

import com.test.api.helper.TestHelper

object EventsModelFactory {

    @JvmStatic
    fun generateJsonOfEvents(): String? {
        return TestHelper.getJsonFromFile("get_events.json")
    }
}
