package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ExploreContentsResponse(
    val contents: List<ExploreContentResponse>,
    val hasMore: Boolean,
    val nextOffset: Long,
    val totalCount: Int,
)
