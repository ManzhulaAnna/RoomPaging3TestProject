package com.test.ui.fragments.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.databinding.ItemEventBinding
import com.test.ui.fragments.events.interfaces.EventOnClickListener
import com.test.ui.fragments.events.model.EventUiModel

class EventsViewHolder(
    parent: ViewGroup,
    private val clickListener: EventOnClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_event, parent, false)
) {
    private val binding = ItemEventBinding.bind(itemView).also {
        it.ivEventFav.setOnClickListener { event?.let { item -> clickListener.onFavoriteClick(item) } }
        it.root.setOnClickListener { event?.let { item -> clickListener.onOpenUrlClick(item) } }
    }
    private var event: EventUiModel? = null

    fun onBind(item: EventUiModel?) {
        event = item
        binding.apply {
            tvEventTitle.text = item?.title
            tvEventSubtitle.text = item?.date
            setFavorite(item)
        }
    }

    fun updateRate(item: EventUiModel?) {
        event = item
        setFavorite(item)
    }

    private fun setFavorite(item: EventUiModel?) {
        binding.ivEventFav.setImageResource(
            if (item?.isFavorite == true) {
                R.drawable.ic_fav
            } else {
                R.drawable.ic_add_fav
            }
        )
    }
}