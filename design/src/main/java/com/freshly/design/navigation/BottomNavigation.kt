package com.freshly.design.navigation

import android.content.Context
import android.util.AttributeSet
import com.freshly.design.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.navigationView
) : BottomNavigationView(context, attrs, defStyleAttr)