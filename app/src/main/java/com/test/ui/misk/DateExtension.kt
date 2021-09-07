package com.test.ui.misk

import com.test.ui.misk.Constants.Date.UI_DATE_FORMAT
import timber.log.Timber
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToUI(
    dateFormat: String = UI_DATE_FORMAT,
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    return try {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        formatter.format(this)
    } catch (e: NullPointerException) {
        Timber.d(e)
        ""
    } catch (e: IllegalArgumentException) {
        Timber.d(e)
        ""
    }
}