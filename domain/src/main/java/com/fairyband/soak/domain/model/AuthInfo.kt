package com.fairyband.soak.domain.model

import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse

data class AuthInfo(
    val deviceToken: String = "",
    val id: Long = 0L
) {
    fun toRequest(): RegisterRequest = RegisterRequest(
        deviceToken = deviceToken
    )
}

fun RegisterResponse.toAuthInfo(): AuthInfo = AuthInfo(
    id = id
)

fun LoginResponse.toAuthInfo(): AuthInfo = AuthInfo(
    id = id
)
