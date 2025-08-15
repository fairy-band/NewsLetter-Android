package com.fairyband.soak.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class FcmRegisterRequest(
    val deviceToken: String,
    val fcmToken: String,
    val deviceType: String,
)
