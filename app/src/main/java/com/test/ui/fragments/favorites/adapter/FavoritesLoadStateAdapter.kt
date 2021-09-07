package com.test.ui.fragments.favorites.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class FavoritesLoadStateAdapter(
    private val adapter: FavoritesAdapter
) : LoadStateAdapter<FavStateItemViewHolder>() {
    override fun onBindViewHolder(holder: FavStateItemViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FavStateItemViewHolder {
        return FavStateItemViewHolder(parent) { adapter.retry() }
    }
}