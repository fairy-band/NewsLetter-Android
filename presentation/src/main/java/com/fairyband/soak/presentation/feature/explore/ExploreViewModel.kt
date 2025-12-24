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

    suspend fun loadFeeds() {
        // TODO: 마지막 페이지를 판단하는 기준이 필요하다. (개수가 나누어 떨어지지 않거나, 새로운 페이지의 크기가 0일 때)
        val pageSize = 20
        val nextPage = news.value.size / 20

        _news.update { existing ->
            val new = newsRepository.getExploreContents(page = nextPage, size = pageSize)
                .map { it.toExploreFeed() }

            existing + new
        }
    }
}