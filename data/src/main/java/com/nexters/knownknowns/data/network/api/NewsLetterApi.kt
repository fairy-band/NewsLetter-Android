package com.nexters.knownknowns.data.network.api

import com.nexters.knownknowns.data.network.model.LetterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface NewsLetterApi {
    @GET("/api/newsletters/contents/{userId}")
    suspend fun getContents(
        @Path("userId") userId: String,
        @Query("publishedDate") publishedDate: String? = null,
    ): List<LetterResponse>
}