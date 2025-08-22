package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.core.extension.toPattern
import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NewsLetterDataSource
import com.fairyband.soak.data.model.response.NewsResponse
import com.fairyband.soak.data.repository.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class NewsRepositoryImpl(
    private val newsLetterDataSource: NewsLetterDataSource,
    private val authDataSource: AuthDataSource,
) : NewsRepository {
    private val refreshFlow = MutableSharedFlow<Unit>()

    // 매일 자정에 뉴스를 새로고침해요.
    private val dayFlow = flow {
        while (true) {
            delay(1_000)
            emit(LocalDate.now().dayOfMonth)
        }
    }.distinctUntilChanged()

    override val news: Flow<List<NewsResponse>> =
        merge(refreshFlow, dayFlow.map { Unit })
            .map {
                val userId = authDataSource.getUserId()
                val publishedDate = LocalDate.now().toPattern("yyyy-MM-dd")

                val response =
                    newsLetterDataSource.getContents(userId = userId, publishedDate = publishedDate)

                response.cards
            }

    override suspend fun invalidateNews() {
        refreshFlow.emit(Unit)
    }
}
