package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val id: Int,
    val title: String,
    val topKeyword: String,
    val summary: String,
    val contentUrl: String,
    val newsletterName: String,
    val language: String,
)
