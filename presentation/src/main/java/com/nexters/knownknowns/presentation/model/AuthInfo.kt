package com.nexters.knownknowns.presentation.model

import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.LoginResponse
import com.nexters.knownknowns.data.model.response.RegisterResponse

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
