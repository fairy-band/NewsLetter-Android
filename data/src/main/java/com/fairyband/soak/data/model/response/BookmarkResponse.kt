package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkResponse(
    val bookmarkId: Long,
    val exposureContentId: Long,
    val contentId: Long,
    val provocativeKeyword: String,
    val provocativeHeadline: String,
    val summaryContent: String,
    val contentUrl: String,
    val imageUrl: String? = null,
    val newsletterName: String,
    val categoryName: String = "",
    val language: String,
    val bookmarkedAt: String,
    val publishedAt: String? = null,
    val estimatedReadingTime: Int = 0,
)
