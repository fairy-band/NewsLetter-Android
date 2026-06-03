package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val id: Long,
    val title: String,
    val topKeyword: String,
    val summary: String,
    val contentUrl: String,
    val imageUrl: String? = null,
    val newsletterName: String,
    val language: String,
    val cardType: String,
)
