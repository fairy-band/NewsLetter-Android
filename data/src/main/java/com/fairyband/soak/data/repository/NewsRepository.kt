package com.fairyband.soak.data.repository

import com.fairyband.soak.core.extension.toPattern
import com.fairyband.soak.data.model.response.NewsResponse
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NewsRepository {
    fun getNews(
        publishedDate: String? = LocalDate.now().toPattern("yyyy-MM-dd")
    ): Flow<List<NewsResponse>>
}
