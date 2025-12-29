package com.fairyband.soak.presentation.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.model.toExploreFeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreViewModel(
    private val newsRepository: NewsRepository,
) : ViewModel() {
    private val _feeds = MutableStateFlow<List<ExploreFeed>>(emptyList())
    val feeds = _feeds.asStateFlow()

    private var isLastPage = false
    private var loadingJob: Job? = null

    fun loadFeeds() {
        if (loadingJob != null || isLastPage) return

        val pageSize = 20
        val nextPage = feeds.value.size / 20

        loadingJob = viewModelScope.launch {
            val newFeeds = newsRepository.getExploreContents(page = nextPage, size = pageSize)
                .map { it.toExploreFeed() }

            if (newFeeds.size < pageSize) {
                isLastPage = true
            }

            _feeds.update { existing ->
                existing + newFeeds
            }
        }

        loadingJob?.invokeOnCompletion {
            loadingJob = null
        }
    }
}