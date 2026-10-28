package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SharedContentsResponse(
    val sharedContents: List<SharedContentResponse>,
    val totalCount: Long,
    val page: Int,
    val size: Int,
    val hasMore: Boolean,
)
