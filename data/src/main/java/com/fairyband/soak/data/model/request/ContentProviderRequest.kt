package com.fairyband.soak.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API 스펙과 프로퍼티 이름이 일치하지 않음에 유의하세요.
 */
@Serializable
data class ContentProviderRequest(
    @SerialName("contentProviderName")
    val name: String,
    @SerialName("channel")
    val url: String,
    @SerialName("requestCategory")
    val position: String,
    @SerialName("relatedTo")
    val language: String,
    @SerialName("reason")
    val reason: String = "",
)
