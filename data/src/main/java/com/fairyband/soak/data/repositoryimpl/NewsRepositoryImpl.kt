package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.core.extension.toPattern
import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NewsLetterDataSource
import com.fairyband.soak.data.local.news.NewsDataStore
import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.NewsResponse
import com.fairyband.soak.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.time.delay
import org.koin.core.annotation.Single
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Single
class NewsRepositoryImpl(
    private val newsLetterDataSource: NewsLetterDataSource,
    private val authDataSource: AuthDataSource,
    private val newsDataStore: NewsDataStore,
) : NewsRepository {
    private val refreshFlow = MutableSharedFlow<Unit>()
    private var nextOffset = 0L

    // 매일 자정에 뉴스를 새로고침해요.
    private val dayFlow = flow {
        while (true) {
            emit(Unit)
            val midnight: LocalDateTime = LocalDate.now().plusDays(1).atStartOfDay()
            val duration = Duration.between(LocalDateTime.now(), midnight)
            Timber.d("뉴스 새로고침까지 ${duration.seconds}초 남았어요.")

            delay(duration)
        }
    }

    override val news: Flow<List<NewsResponse>> =
        merge(refreshFlow, dayFlow)
            .map {
                Timber.d("뉴스를 새로 불러왔어요.")

                val userId = authDataSource.getUserId()
                val publishedDate = LocalDate.now().toPattern("yyyy-MM-dd")

                val response =
                    newsLetterDataSource.getContents(userId = userId, publishedDate = publishedDate)

                response.cards
            }

    override val hasRefreshedToday: Flow<Boolean> = newsDataStore.hasRefreshedToday

    override suspend fun invalidateNews() {
        refreshFlow.emit(Unit)
    }

    override suspend fun refreshNews() {
        val userId = authDataSource.getUserId()
        newsLetterDataSource.refreshContents(userId)
        newsDataStore.recordRefreshToday()
        refreshFlow.emit(Unit)
    }

    override suspend fun getExploreContents(): ExploreContentsResponse {
        val response = newsLetterDataSource.getExploreContents(nextOffset)

        nextOffset = response.nextOffset

        return response
    }

    override suspend fun requestContentProvider(request: ContentProviderRequest) {
        newsLetterDataSource.requestContentProvider(request)
    }
}