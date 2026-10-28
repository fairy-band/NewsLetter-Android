package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SharedContentResponse(
    val sharedContentId: Long,
    val exposureContentId: Long,
    val contentId: Long,
    val provocativeKeyword: String,
    val provocativeHeadline: String,
    val summaryContent: String,
    val contentUrl: String,
    val imageUrl: String? = null,
    val newsletterName: String,
    val categoryName: String,
    val language: String,
    val sharedAt: String,
    val createdAt: String,
    val publishedAt: String? = null,
    val estimatedReadingTime: Int = 0,
)
