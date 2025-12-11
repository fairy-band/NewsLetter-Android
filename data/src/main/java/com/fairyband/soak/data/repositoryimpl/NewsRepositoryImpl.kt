package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.core.extension.toPattern
import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NewsLetterDataSource
import com.fairyband.soak.data.model.response.ExploreContentResponse
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
) : NewsRepository {
    private val refreshFlow = MutableSharedFlow<Unit>()

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

    override suspend fun invalidateNews() {
        refreshFlow.emit(Unit)
    }

    override fun getExploreContents(page: Int, size: Int): Flow<List<ExploreContentResponse>> = flow {
        val response = newsLetterDataSource.getExploreContents(page, size)
        emit(response.contents)
    }
}
