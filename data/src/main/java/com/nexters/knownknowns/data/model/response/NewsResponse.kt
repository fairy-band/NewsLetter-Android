package com.nexters.knownknowns.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val id: String,
    val title: String,
    val keyword: String,
    val letter: String,
    val summary: String,
    val url: String,
)
