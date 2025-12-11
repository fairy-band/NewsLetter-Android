package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ExploreContentResponse(
    val id: Int,
    val contentId: Int,
    val provocativeKeyword: String,
    val provocativeHeadline: String,
    val summaryContent: String,
    val contentUrl: String,
    val newsletterName: String,
    val createdAt: String,
    val updatedAt: String,
)
