package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.datasource.NewsLetterDataSource
import com.nexters.knownknowns.data.model.response.NewsResponse
import com.nexters.knownknowns.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class NewsRepositoryImpl(
    private val newsLetterDataSource: NewsLetterDataSource
) : NewsRepository {
    override fun getNews(userId: Long, publishedDate: String?): Flow<List<NewsResponse>> = flow {
        val response =
            newsLetterDataSource.getContents(userId = userId, publishedDate = publishedDate)
        emit(response.cards)
    }
}
