package com.fairyband.soak.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentProviderRequest(
    val contentProviderName: String,
    val channel: String,
    val requestCategory: String,
    val relatedTo: String,
    val reason: String,
)
