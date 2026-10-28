package com.fairyband.soak.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkRequest(
    val userId: Long,
    val exposureContentId: Long,
    val isBookmarked: Boolean,
)
