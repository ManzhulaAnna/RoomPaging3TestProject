package com.test.ui.fragments.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.databinding.LoadingFavoriteStateItemBinding

class FavStateItemViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.loading_favorite_state_item, parent, false)
) {
    private val binding = LoadingFavoriteStateItemBinding.bind(itemView).also {
        it.loadFavRetryButton.setOnClickListener { retryCallback() }
    }

    fun onBind(loadState: LoadState) {
        binding.apply {
            loadFavProgressBar.isVisible = loadState is LoadState.Loading
            loadFavRetryButton.isVisible = loadState is LoadState.Error
            loadFavErrorText.isVisible =
                !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            loadFavErrorText.text = (loadState as? LoadState.Error)?.error?.message
        }
    }
}