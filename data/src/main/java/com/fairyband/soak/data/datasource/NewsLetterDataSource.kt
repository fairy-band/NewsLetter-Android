package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.LetterResponse
import com.fairyband.soak.data.remote.service.NewsLetterService
import org.koin.core.annotation.Singleton

@Singleton
class NewsLetterDataSource(
    private val api: NewsLetterService,
) {
    suspend fun getContents(
        userId: Long,
        publishedDate: String?
    ): LetterResponse {
        return api.getContents(userId, publishedDate)
    }

    suspend fun getExploreContents(
        nextOffset: Long
    ): ExploreContentsResponse {
        return api.getExploreContents(lastSeenOffset = nextOffset, size = 20)
    }

    suspend fun refreshContents(userId: Long) {
        api.refreshContents(userId)
    }

    suspend fun requestContentProvider(request: ContentProviderRequest) {
        api.requestContentProvider(request)
    }
}