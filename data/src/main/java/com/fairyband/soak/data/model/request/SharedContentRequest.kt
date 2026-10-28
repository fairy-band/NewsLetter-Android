package com.fairyband.soak.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SharedContentRequest(
    val userId: Long,
    val exposureContentId: Long,
    val isShared: Boolean,
)
