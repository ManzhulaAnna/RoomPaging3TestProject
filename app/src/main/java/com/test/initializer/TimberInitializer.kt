@file:Suppress("unused")

package com.test.initializer

import android.content.Context
import androidx.annotation.Keep
import androidx.startup.Initializer
import androidx.viewbinding.BuildConfig
import timber.log.Timber

@Keep
class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
