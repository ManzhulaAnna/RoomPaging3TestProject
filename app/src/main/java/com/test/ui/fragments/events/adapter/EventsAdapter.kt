package com.test.ui.fragments.events.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.test.ui.fragments.events.interfaces.EventOnClickListener
import com.test.ui.fragments.events.model.EventUiModel

class EventsAdapter(private val clickListener: EventOnClickListener) :
    PagingDataAdapter<EventUiModel, EventsViewHolder>(EVENTS_COMPARATOR) {

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: EventsViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            holder.updateRate(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(parent, clickListener)
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val EVENTS_COMPARATOR = object : DiffUtil.ItemCallback<EventUiModel>() {
            override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
                oldItem.isFavorite == newItem.isFavorite
                        && oldItem.title == newItem.title
                        && oldItem.date == newItem.date
                        && oldItem.url == newItem.url

            override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: EventUiModel, newItem: EventUiModel): Any? {
                return if (sameExceptFavorite(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptFavorite(oldItem: EventUiModel, newItem: EventUiModel): Boolean {
            return oldItem.copy(isFavorite = newItem.isFavorite) == newItem
        }
    }
}
