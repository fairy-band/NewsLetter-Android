package com.fairyband.soak.data.remote.service

import com.fairyband.soak.data.model.request.BookmarkRequest
import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.request.SharedContentRequest
import com.fairyband.soak.data.model.response.BookmarkStatusResponse
import com.fairyband.soak.data.model.response.BookmarksResponse
import com.fairyband.soak.data.model.response.ExploreContentsResponse
import com.fairyband.soak.data.model.response.LetterResponse
import com.fairyband.soak.data.model.response.MarkdownResponse
import com.fairyband.soak.data.model.response.SharedContentStatusResponse
import com.fairyband.soak.data.model.response.SharedContentsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
        @Query("sort")
        sort: String = "PUBLISHED",
        @Query("direction")
        direction: String,
        @Query("categoryIds")
        categoryIds: List<String>? = null,
    ): ExploreContentsResponse

    @GET("api/newsletters/exposure-contents/{exposureContentId}/markdown")
    suspend fun getMarkdown(
        @Path("exposureContentId") exposureContentId: Long,
    ): MarkdownResponse

    @POST("api/newsletters/contents/{userId}/refresh")
    suspend fun refreshContents(
        @Path("userId") userId: Long,
    )

    @POST("api/newsletters/content-provider-requests")
    suspend fun requestContentProvider(
        @Body request: ContentProviderRequest,
    )

    @GET("api/newsletters/bookmarks/{userId}")
    suspend fun getBookmarks(
        @Path("userId") userId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): BookmarksResponse

    @PUT("api/newsletters/bookmarks")
    suspend fun updateBookmark(
        @Body request: BookmarkRequest,
    ): BookmarkStatusResponse

    @GET("api/newsletters/shared-contents/{userId}")
    suspend fun getSharedContents(
        @Path("userId") userId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): SharedContentsResponse

    @PUT("api/newsletters/shared-contents")
    suspend fun updateSharedContent(
        @Body request: SharedContentRequest,
    ): SharedContentStatusResponse
}
