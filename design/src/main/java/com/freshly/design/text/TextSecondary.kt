package com.freshly.design.text

import android.content.Context
import android.util.AttributeSet
import com.freshly.design.R
import com.google.android.material.textview.MaterialTextView

class TextSecondary @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.fontStyleSecondary
) : MaterialTextView(context, attrs, defStyleAttr)