package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LetterResponse(
    val publishedDate: String,
    val trendingCard: NewsResponse? = null,
    val cards: List<NewsResponse>,
)