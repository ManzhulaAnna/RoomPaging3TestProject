package com.test.ui.fragments.favorites.interfaces

import com.test.ui.fragments.favorites.model.FavoriteEventUiModel

interface FavoritesOnClickListener {
    fun onFavoriteClick(event: FavoriteEventUiModel) {}
    fun onOpenUrlClick(event: FavoriteEventUiModel) {}
}