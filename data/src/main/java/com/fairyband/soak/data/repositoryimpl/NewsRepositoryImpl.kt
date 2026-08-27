package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.core.extension.toPattern
import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NewsLetterDataSource
import com.fairyband.soak.data.local.news.NewsDataStore
import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.request.Direction
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.LetterResponse
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
    private var currentDirection: Direction = Direction.DESC
    private var currentCategoryIds: List<String> = emptyList()

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

    override val news: Flow<LetterResponse> =
        merge(refreshFlow, dayFlow)
            .map {
                Timber.d("뉴스를 새로 불러왔어요.")

                val userId = authDataSource.getUserId()
                val publishedDate = LocalDate.now().toPattern("yyyy-MM-dd")

                newsLetterDataSource.getContents(userId = userId, publishedDate = publishedDate)
            }

    override val hasRefreshedToday: Flow<Boolean> = newsDataStore.hasRefreshedToday

    override suspend fun invalidateNews() {
        refreshFlow.emit(Unit)
    }

    override fun refreshNews(): Flow<Unit> = flow {
        val userId = authDataSource.getUserId()
        newsLetterDataSource.refreshContents(userId)
        newsDataStore.recordRefreshToday()
        refreshFlow.emit(Unit)
        emit(Unit)
    }

    override suspend fun getExploreContents(
        direction: Direction?,
        categoryIds: List<String>,
    ): ExploreContentsResponse {
        // keyset 커서는 정렬/필터 조합에 종속적이라, 조건이 바뀌면 처음부터 다시 조회해야 해요.
        if (direction != null && direction != currentDirection) {
            nextOffset = 0L
            currentDirection = direction
        }
        if (categoryIds != currentCategoryIds) {
            nextOffset = 0L
            currentCategoryIds = categoryIds
        }
        val response = newsLetterDataSource.getExploreContents(
            nextOffset = nextOffset,
            direction = currentDirection,
            categoryIds = currentCategoryIds.ifEmpty { null },
        )
        nextOffset = response.nextOffset
        return response
    }

    override suspend fun getMarkdown(exposureContentId: Long): String {
        return newsLetterDataSource.getMarkdown(exposureContentId).markdownContent
    }

    override suspend fun requestContentProvider(request: ContentProviderRequest) {
        newsLetterDataSource.requestContentProvider(request)
    }
}