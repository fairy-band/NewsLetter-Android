package com.fairyband.soak.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    @SerialName("preferences")
    val preferences: List<String>,
    @SerialName("workingExperience")
    val workingExperience: String,
)
