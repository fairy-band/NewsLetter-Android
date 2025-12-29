package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.response.ExploreContentResponse
import com.fairyband.soak.data.model.response.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val news: Flow<List<NewsResponse>>

    suspend fun invalidateNews()
    suspend fun getExploreContents(page: Int = 0, size: Int = 20): List<ExploreContentResponse>
}
