package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BookmarksResponse(
    val bookmarks: List<BookmarkResponse>,
    val totalCount: Long,
    val page: Int,
    val size: Int,
    val hasMore: Boolean,
)
