package com.nexters.knownknowns.data.remote.service

import com.nexters.knownknowns.data.model.response.LetterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsLetterService {
    @GET("api/newsletters/contents/{userId}")
    suspend fun getContents(
        @Path("userId") userId: Long,
        @Query("publishedDate") publishedDate: String? = null,
    ): LetterResponse
}