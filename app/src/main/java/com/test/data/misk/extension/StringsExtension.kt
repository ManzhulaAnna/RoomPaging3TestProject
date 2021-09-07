package com.test.data.misk.extension

import com.test.data.misk.Constants.Date.BACKEND_DATE_FORMAT
import com.test.data.misk.Constants.Date.BACKEND_DATE_TIMEZONE
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String?.parseDate(): Date? {
    if (this == null) {
        return null
    }

    val format = SimpleDateFormat(BACKEND_DATE_FORMAT, Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone(BACKEND_DATE_TIMEZONE)
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}
