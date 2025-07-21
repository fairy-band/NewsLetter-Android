package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.model.NewsResponse
import com.nexters.knownknowns.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class NewsRepositoryImpl : NewsRepository {
    private val _news = listOf(NewsResponse(id = "1", title = "haha"))

    override fun getNews(): Flow<List<NewsResponse>> = flow {
        emit(_news.toList())
    }
}