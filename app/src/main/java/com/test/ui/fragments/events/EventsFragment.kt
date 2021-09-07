package com.test.ui.fragments.events

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.test.R
import com.test.databinding.FragmentEventsBinding
import com.test.ui.fragments.BaseFragment
import com.test.ui.fragments.events.adapter.EventsAdapter
import com.test.ui.fragments.events.adapter.EventsLoadStateAdapter
import com.test.ui.fragments.events.adapter.decoration.ItemDecoration
import com.test.ui.misk.getDimenInPixel
import com.test.ui.misk.openExternalBrowser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    private val viewModel: EventsViewModel by viewModels()

    private val adapter by lazy {
        EventsAdapter(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeToRefresh()
        initAdapter()
        observeUiModel()
    }

    private fun observeUiModel() {
        viewModel.uiModel.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorEvent ->
            showError(errorEvent.error)
        }

        viewModel.urlClickEvent.observe(viewLifecycleOwner) { url ->
            openExternalBrowser(url)
        }
    }

    private fun initAdapter() {
        binding.rvEvents.adapter = adapter.withLoadStateFooter(
            footer = EventsLoadStateAdapter(adapter)
        )

        binding.rvEvents.addItemDecoration(
            ItemDecoration(
                context.getDimenInPixel(R.dimen.item_horizontal_padding),
                context.getDimenInPixel(R.dimen.item_vertical_padding)
            )
        )

        lifecycleScope.launchWhenCreated {
            adapter.addLoadStateListener { loadState ->
                when (val source = loadState.refresh) {
                    is LoadState.Error -> showError(source.error.message)
                    is LoadState.NotLoading -> {
                        if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                            showText(context?.getString(R.string.no_events_found))
                        } else {
                            hideError()
                        }
                    }
                    else -> hideError()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefreshEvents
                    .isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefreshEvents.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun showError(message: String?) {
        binding.tvErrorTextEvents.text = getString(R.string.error_message_holder, message)
        binding.tvErrorTextEvents.isVisible = true
    }

    private fun hideError() {
        binding.tvErrorTextEvents.text = ""
        binding.tvErrorTextEvents.isVisible = false
    }

    private fun showText(message: String?) {
        binding.tvErrorTextEvents.text = message
        binding.tvErrorTextEvents.isVisible = true
    }
}
