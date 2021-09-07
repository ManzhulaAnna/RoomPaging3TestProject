package com.freshly.design.progress

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.freshly.design.R

class LoadingProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.loadingProgress
) : ProgressBar(context, attrs, defStyleAttr)