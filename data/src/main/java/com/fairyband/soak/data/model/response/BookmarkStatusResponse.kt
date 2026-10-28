package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkStatusResponse(
    val userId: Long,
    val exposureContentId: Long,
    val isBookmarked: Boolean,
)
