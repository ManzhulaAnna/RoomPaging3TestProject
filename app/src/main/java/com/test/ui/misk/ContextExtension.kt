package com.test.ui.misk

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DimenRes
import timber.log.Timber

internal fun Context?.getDimenInPixel(@DimenRes resource: Int): Int {
    return try {
        this?.resources?.getDimensionPixelSize(resource) ?: 0
    } catch (e: Resources.NotFoundException) {
        Timber.e(e)
        0
    }
}