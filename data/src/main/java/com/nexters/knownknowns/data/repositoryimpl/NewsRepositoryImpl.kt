package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.model.NewsResponse
import com.nexters.knownknowns.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class NewsRepositoryImpl : NewsRepository {
    private val _news = listOf(
        NewsResponse(id = "1", title = " 도망 간 슬픈 이야기. 여름이었다.", keyword = "Kotlin", letter = "Android Weekly", summary = "", url = "https://www.naver.com"),
        NewsResponse(id = "2", title = " 도망 간 슬픈 이야기. 여름이었다.", keyword = "Kotlin", letter = "Android Weekly", summary = "", url = "https://www.naver.com"),
        NewsResponse(id = "3", title = " 도망 간 슬픈 이야기. 여름이었다.", keyword = "Kotlin", letter = "Android Weekly", summary = "", url = "https://www.naver.com"),
        NewsResponse(id = "4", title = "", keyword = "Kotlin", letter = "Android Weekly", summary = "", url = "https://www.naver.com"),
        NewsResponse(id = "5", title = " 도망 간 슬픈 이야기. 여름이었다.", keyword = "Kotlin", letter = "Android Weekly", summary = "", url = "https://www.naver.com"),
        NewsResponse(id = "6", title = " 도망 간 슬픈 이야기.", keyword = "Kotlin", letter = "Android Weekly", summary = "", url = "https://www.naver.com"),
    )

    override fun getNews(): Flow<List<NewsResponse>> = flow {
        emit(_news.toList())
    }
}