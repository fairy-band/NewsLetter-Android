package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.auth.AuthDataStore
import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse
import com.fairyband.soak.data.remote.service.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class AuthDataSource(
    private val authDataStore: AuthDataStore,
    private val authService: AuthService,
) {
    suspend fun getUserId(): Long {
        return authDataStore.userId.first() ?: loginUser().id
    }

    private suspend fun setUserId(id: Long) {
        authDataStore.setUserId(id)
    }

    suspend fun getDeviceToken(): String = authDataStore.deviceToken.first()

    suspend fun registerUser(request: RegisterRequest): RegisterResponse =
        authService.registerUser(
            body = request
        ).apply {
            setUserId(id)
        }

    suspend fun loginUser(): LoginResponse {
        return authService.loginUser(
            deviceToken = getDeviceToken()
        ).apply {
            setUserId(id)
        }
    }
}
