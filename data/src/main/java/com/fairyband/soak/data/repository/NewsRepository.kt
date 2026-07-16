package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.request.Direction
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.LetterResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val news: Flow<LetterResponse>
    val hasRefreshedToday: Flow<Boolean>

    suspend fun invalidateNews()
    fun refreshNews(): Flow<Unit>
    /**
     * @param categoryIds 직군 카테고리 ID 목록. 비어 있으면 전체 조회예요.
     */
    suspend fun getExploreContents(
        direction: Direction?,
        categoryIds: List<String> = emptyList(),
    ): ExploreContentsResponse
    suspend fun requestContentProvider(request: ContentProviderRequest)
}
