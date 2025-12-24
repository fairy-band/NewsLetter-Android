package com.fairyband.soak.presentation.feature.explore

import androidx.lifecycle.ViewModel
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.model.toExploreFeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreViewModel(
    private val newsRepository: NewsRepository,
) : ViewModel() {
    private val _news = MutableStateFlow<List<ExploreFeed>>(emptyList())
    val news = _news.asStateFlow()

    private var isLastPage = false

    suspend fun loadFeeds() {
        if (isLastPage) return

        val pageSize = 20
        val nextPage = news.value.size / 20

        _news.update { existing ->
            val new = newsRepository.getExploreContents(page = nextPage, size = pageSize)
                .map { it.toExploreFeed() }

            if (new.size < pageSize) {
                isLastPage = true
            }

            existing + new
        }
    }
}