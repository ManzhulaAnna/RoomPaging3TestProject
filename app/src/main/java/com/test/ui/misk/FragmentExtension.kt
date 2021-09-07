package com.test.ui.misk

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.test.R
import timber.log.Timber

internal fun Fragment.openExternalBrowser(url: String?) {
    activity?.openExternalBrowser(url)
}

internal fun FragmentActivity.openExternalBrowser(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }

    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )

    //if (browserIntent.resolveActivity(packageManager) != null)
    //https://developer.android.com/training/package-visibility
    try {
        startActivity(browserIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            this,
            getString(R.string.no_app_to_open_url),
            Toast.LENGTH_SHORT
        ).show()

        Timber.e(e)
    }
}