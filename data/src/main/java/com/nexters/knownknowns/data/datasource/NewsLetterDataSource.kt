package com.nexters.knownknowns.data.datasource

import com.nexters.knownknowns.data.model.response.LetterResponse
import com.nexters.knownknowns.data.remote.service.NewsLetterService
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