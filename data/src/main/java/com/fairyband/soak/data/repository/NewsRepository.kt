package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.request.Sort
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.LetterResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val news: Flow<LetterResponse>
    val hasRefreshedToday: Flow<Boolean>

    suspend fun invalidateNews()
    fun refreshNews(): Flow<Unit>
    suspend fun getExploreContents(sort: Sort?): ExploreContentsResponse
    suspend fun requestContentProvider(request: ContentProviderRequest)
}
