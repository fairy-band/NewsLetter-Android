package com.nexters.knownknowns.data.network.api

import com.nexters.knownknowns.data.network.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

internal interface NewsLetterApi {
    @GET("/api/newsletters/contents/{userId}")
    suspend fun getContents(
        @Path("userId") userId: String,
        @Query("publishedDate") publishedDate: LocalDate? = null,
    ): List<NewsResponse>
}