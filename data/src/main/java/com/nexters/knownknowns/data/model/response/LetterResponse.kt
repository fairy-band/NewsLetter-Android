package com.nexters.knownknowns.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LetterResponse(
    val publishedDate: String,
    val cards: List<NewsResponse>,
)