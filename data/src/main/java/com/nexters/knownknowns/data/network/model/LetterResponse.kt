package com.nexters.knownknowns.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LetterResponse(
    val publishedDate: String,
    val cards: List<NewsResponse>,
)
