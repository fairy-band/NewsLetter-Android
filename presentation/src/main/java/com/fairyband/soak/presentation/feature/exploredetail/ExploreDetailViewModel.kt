package com.fairyband.soak.presentation.feature.exploredetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.model.toExploreFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreDetailViewModel(
    detail: MainDestination.ExploreDetail,
    private val newsRepository: NewsRepository,
) : ViewModel() {
    private val _totalCount = MutableStateFlow(0)
    val totalCount = _totalCount.asStateFlow()

    private val _feeds: MutableStateFlow<List<ExploreFeed>> = MutableStateFlow(detail.feeds)
    val feeds = _feeds.asStateFlow()

    private val _selectedIndex = MutableStateFlow(detail.index)
    val selectedIndex = _selectedIndex.asStateFlow()

    private var hasMore = true
    private var loadingJob: Job? = null

    fun loadFeeds() {
        if (loadingJob != null || !hasMore) return

        val pageSize = 20
        val nextPage = feeds.value.size / 20

        loadingJob = viewModelScope.launch {
            val response = newsRepository.getExploreContents(page = nextPage, size = pageSize)
            _totalCount.update { response.totalCount }

            val newFeeds = response.contents.map { it.toExploreFeed() }
            hasMore = response.hasMore

            _feeds.update { existing ->
                existing + newFeeds
            }
        }

        loadingJob?.invokeOnCompletion {
            loadingJob = null
        }
    }

    fun selectFeed(index: Int) {
        _selectedIndex.update { index }
    }
}