package com.fairyband.soak.data.remote.service

import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.LetterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsLetterService {
    @GET("api/newsletters/contents/{userId}")
    suspend fun getContents(
        @Path("userId") userId: Long,
        @Query("publishedDate") publishedDate: String? = null,
    ): LetterResponse

    @GET("api/newsletters/explore/contents")
    suspend fun getExploreContents(
        @Query("lastSeenOffset")
        lastSeenOffset: Long = 0,
        @Query("size")
        size: Int = 20,
    ): ExploreContentsResponse

    @POST("api/newsletters/content-provider-requests")
    suspend fun requestContentProvider(
        @Body request: ContentProviderRequest,
    )
}