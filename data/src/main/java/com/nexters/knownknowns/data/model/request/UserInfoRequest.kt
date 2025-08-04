package com.nexters.knownknowns.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    @SerialName("preference")
    val preference: List<String>,
    @SerialName("workingExperience")
    val workingExperience: String,
)
