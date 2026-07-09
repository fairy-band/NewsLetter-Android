package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val id: Long,
    val preferences: List<String> = emptyList(),
    val workingExperience: String? = null,
)
