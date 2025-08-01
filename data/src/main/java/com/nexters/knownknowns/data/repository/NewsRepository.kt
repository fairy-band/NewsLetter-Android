package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.core.extension.toPattern
import com.nexters.knownknowns.data.model.response.NewsResponse
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NewsRepository {
    fun getNews(
        userId: Long,
        publishedDate: String? = LocalDate.now().toPattern("yyyy-MM-dd")
    ): Flow<List<NewsResponse>>
}
