package com.test.data.local

import android.app.Application
import android.content.Context
import com.test.data.misk.extension.save
import javax.inject.Inject

class DataStatePrefs @Inject constructor(
    application: Application,
) {

    private val preference =
        application.getSharedPreferences("DatePreference", Context.MODE_PRIVATE)

    var lastUpdate: Long
        set(value) = preference.save(LAST_UPDATE_AT, value)
        get() = preference.getLong(LAST_UPDATE_AT, 0)

    companion object {
        private const val LAST_UPDATE_AT = "LAST_UPDATE_AT"
    }
}