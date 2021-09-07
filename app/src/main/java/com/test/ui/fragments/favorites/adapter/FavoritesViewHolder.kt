package com.test.ui.fragments.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.databinding.ItemEventFavoriteBinding
import com.test.ui.fragments.favorites.interfaces.FavoritesOnClickListener
import com.test.ui.fragments.favorites.model.FavoriteEventUiModel

class FavoritesViewHolder(
    parent: ViewGroup,
    private val clickListener: FavoritesOnClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_event_favorite, parent, false)
) {
    private val binding = ItemEventFavoriteBinding.bind(itemView).also {
        it.ivFavEventFav
            .setOnClickListener { event?.let { item -> clickListener.onFavoriteClick(item) } }
        it.root.setOnClickListener { event?.let { item -> clickListener.onOpenUrlClick(item) } }
    }
    private var event: FavoriteEventUiModel? = null

    fun onBind(item: FavoriteEventUiModel?) {
        event = item
        binding.apply {
            tvFavEventTitle.text = item?.title
            tvFavEventSubtitle.text = item?.date
        }
    }
}