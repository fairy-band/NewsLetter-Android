package com.fairyband.soak.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("id")
    val id: Long
)
