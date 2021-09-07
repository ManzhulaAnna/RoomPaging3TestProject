package com.test.ui.fragments.events.interfaces

import com.test.ui.fragments.events.model.EventUiModel

interface EventOnClickListener {
    fun onFavoriteClick(event: EventUiModel) {}
    fun onOpenUrlClick(event: EventUiModel) {}
}