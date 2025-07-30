package com.nexters.knownknowns.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val title: String,
    val topKeyword: String,
    val newsletterName: String,
    val summary: String,
    val contentUrl: String,
)
