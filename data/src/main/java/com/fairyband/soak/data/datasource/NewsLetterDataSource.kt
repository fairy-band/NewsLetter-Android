package com.fairyband.soak.data.datasource

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
}