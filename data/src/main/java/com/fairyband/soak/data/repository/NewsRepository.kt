package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val news: Flow<List<NewsResponse>>
    val hasRefreshedToday: Flow<Boolean>

    suspend fun invalidateNews()
    suspend fun refreshNews()
    suspend fun getExploreContents(): ExploreContentsResponse
    suspend fun requestContentProvider(request: ContentProviderRequest)
}
