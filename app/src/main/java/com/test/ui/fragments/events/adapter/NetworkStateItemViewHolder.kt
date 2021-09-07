package com.test.ui.fragments.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.databinding.NetworkStateItemBinding

class NetworkStateItemViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {
    private val binding = NetworkStateItemBinding.bind(itemView).also {
        it.notworkLoadingRetryButton.setOnClickListener { retryCallback() }
    }

    fun onBind(loadState: LoadState) {
        binding.apply {
            notworkLoadingProgressBar.isVisible = loadState is LoadState.Loading
            notworkLoadingRetryButton.isVisible = loadState is LoadState.Error
            notworkLoadingErrorText.isVisible =
                !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            notworkLoadingErrorText.text = (loadState as? LoadState.Error)?.error?.message
        }
    }
}