package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MarkdownResponse(
    val exposureContentId: Long,
    val markdownContent: String,
)
