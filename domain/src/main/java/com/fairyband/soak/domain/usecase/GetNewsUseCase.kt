package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.domain.model.toNewsFeed
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetNewsUseCase(
    newsRepository: NewsRepository,
) {
    val news = newsRepository.news.map { response ->
        listOfNotNull(response.trendingCard?.toNewsFeed(isTrending = true)) +
                response.cards
                    .filter { it.id != response.trendingCard?.id }
                    .map { it.toNewsFeed() }
    }
}