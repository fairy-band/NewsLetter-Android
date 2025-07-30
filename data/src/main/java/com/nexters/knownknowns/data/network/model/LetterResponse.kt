package com.nexters.knownknowns.data.network.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class LetterResponse(
    val publishedDate: LocalDate,
    val cards: List<NewsResponse>,
)
