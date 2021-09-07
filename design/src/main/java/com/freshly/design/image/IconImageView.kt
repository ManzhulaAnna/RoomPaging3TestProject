package com.freshly.design.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.freshly.design.R

class IconImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.iconImageView
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val minMeasureWidth = context.resources.getDimensionPixelSize(R.dimen.icon_width_min)
    private val maxMeasureWidth = context.resources.getDimensionPixelSize(R.dimen.icon_width_max)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        var width = (height * 0.7).toInt()
        if (minMeasureWidth > measuredWidth) {
            width = minMeasureWidth
        }
        if (maxMeasureWidth < measuredWidth) {
            width = maxMeasureWidth
        }
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            heightMeasureSpec
        )
    }

}