package com.freshly.design.text

import android.content.Context
import android.util.AttributeSet
import com.freshly.design.R
import com.google.android.material.textview.MaterialTextView

class TextInformation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.fontStyleInfoText
) : MaterialTextView(context, attrs, defStyleAttr)