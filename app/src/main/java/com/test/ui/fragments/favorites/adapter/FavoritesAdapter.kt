package com.test.ui.fragments.favorites.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.test.ui.fragments.favorites.interfaces.FavoritesOnClickListener
import com.test.ui.fragments.favorites.model.FavoriteEventUiModel

class FavoritesAdapter(private val clickListener: FavoritesOnClickListener) :
    PagingDataAdapter<FavoriteEventUiModel, FavoritesViewHolder>(FAVORITES_COMPARATOR) {

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(parent, clickListener)
    }

    companion object {
        val FAVORITES_COMPARATOR = object : DiffUtil.ItemCallback<FavoriteEventUiModel>() {
            override fun areContentsTheSame(
                oldItem: FavoriteEventUiModel,
                newItem: FavoriteEventUiModel
            ): Boolean =
                oldItem.title == newItem.title
                        && oldItem.date == newItem.date
                        && oldItem.url == newItem.url

            override fun areItemsTheSame(
                oldItem: FavoriteEventUiModel,
                newItem: FavoriteEventUiModel
            ): Boolean =
                oldItem.id == newItem.id
        }
    }
}
