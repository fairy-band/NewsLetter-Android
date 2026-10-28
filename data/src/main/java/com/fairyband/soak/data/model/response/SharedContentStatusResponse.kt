package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SharedContentStatusResponse(
    val userId: Long,
    val exposureContentId: Long,
    val isShared: Boolean,
)
