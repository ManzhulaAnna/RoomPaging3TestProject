package com.test.ui.fragments.favorites.callback

import androidx.recyclerview.widget.DiffUtil
import com.test.ui.fragments.favorites.model.FavoriteEventUiModel

class DiffFavoriteEventCallback : DiffUtil.ItemCallback<FavoriteEventUiModel>() {
    override fun areItemsTheSame(
        oldItem: FavoriteEventUiModel,
        newItem: FavoriteEventUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FavoriteEventUiModel,
        newItem: FavoriteEventUiModel
    ): Boolean {
        return oldItem == newItem
    }
}