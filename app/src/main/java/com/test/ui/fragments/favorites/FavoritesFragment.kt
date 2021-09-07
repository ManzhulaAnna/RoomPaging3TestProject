package com.test.ui.fragments.favorites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.test.R
import com.test.databinding.FragmentFavEventsBinding
import com.test.ui.fragments.BaseFragment
import com.test.ui.fragments.favorites.adapter.FavoritesAdapter
import com.test.ui.fragments.favorites.adapter.FavoritesLoadStateAdapter
import com.test.ui.fragments.favorites.adapter.decoration.FavItemDecoration
import com.test.ui.misk.getDimenInPixel
import com.test.ui.misk.openExternalBrowser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavEventsBinding>(FragmentFavEventsBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    private val adapter by lazy {
        FavoritesAdapter(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeUiModel()
    }

    private fun observeUiModel() {
        viewModel.uiModel.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorEvent ->
            showText(getString(R.string.error_message_holder, errorEvent.error))
        }

        viewModel.urlClickEvent.observe(viewLifecycleOwner) { url ->
            openExternalBrowser(url)
        }
    }

    private fun initAdapter() {
        binding.rvFavEvents.adapter = adapter.withLoadStateFooter(
            footer = FavoritesLoadStateAdapter(adapter)
        )

        binding.rvFavEvents.addItemDecoration(
            FavItemDecoration(
                context.getDimenInPixel(R.dimen.item_horizontal_padding),
                context.getDimenInPixel(R.dimen.item_vertical_padding)
            )
        )

        lifecycleScope.launchWhenCreated {
            adapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached
                    && adapter.itemCount < 1
                ) {
                    showText(context?.getString(R.string.no_favorites_found))
                } else {
                    hideText()
                }
            }
        }
    }

    private fun showText(message: String?) {
        binding.tvFavNoDataText.text = message
        binding.tvFavNoDataText.isVisible = true
    }

    private fun hideText() {
        binding.tvFavNoDataText.text = ""
        binding.tvFavNoDataText.isVisible = false
    }
}
