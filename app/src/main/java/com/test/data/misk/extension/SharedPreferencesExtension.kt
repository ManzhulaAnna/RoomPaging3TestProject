package com.test.data.misk.extension

import android.annotation.SuppressLint
import android.content.SharedPreferences

@SuppressLint("ApplySharedPref")
fun SharedPreferences.save(
    key: String,
    value: Long,
    immediately: Boolean = false
) {
    if (immediately) {
        edit().putLong(key, value).commit()
    } else {
        edit().putLong(key, value).apply()
    }
}