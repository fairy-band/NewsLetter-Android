package com.nexters.knownknowns.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("deviceToken")
    val deviceToken: String
)
