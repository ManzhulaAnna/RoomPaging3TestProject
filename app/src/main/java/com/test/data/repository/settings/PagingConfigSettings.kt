package com.test.data.repository.settings

import androidx.paging.PagingConfig

class PagingConfigSettings {
    companion object Factory {
        private const val PAGE_SIZE = 30
        private const val PREFETCH_DISTANCE = 30
        private const val MAX_SIZE = PAGE_SIZE * 5

        fun create() = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            initialLoadSize = PAGE_SIZE * 2,
            enablePlaceholders = false,
            maxSize = MAX_SIZE,
        )
    }
}