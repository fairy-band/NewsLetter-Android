package com.nexters.knownknowns.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val title: String,
    val topKeyword: String,
    val summary: String,
    val contentUrl: String,
    val newsletterName: String,
)
