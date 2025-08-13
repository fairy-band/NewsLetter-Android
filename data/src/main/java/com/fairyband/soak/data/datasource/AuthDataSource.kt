package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.auth.AuthDataStore
import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse
import com.fairyband.soak.data.remote.service.AuthService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AuthDataSource(
    private val authDataStore: AuthDataStore,
    private val authService: AuthService,
) {
    fun getUserId(): Flow<Long?> = authDataStore.userId

    private suspend fun setUserId(id: Long) {
        authDataStore.setUserId(id)
    }

    fun getDeviceToken(): Flow<String> = authDataStore.deviceToken

    suspend fun registerUser(request: RegisterRequest): RegisterResponse =
        authService.registerUser(
            body = request
        ).apply {
            setUserId(id)
        }

    suspend fun loginUser(deviceToken: String): LoginResponse =
        authService.loginUser(
            deviceToken = deviceToken
        ).apply {
            setUserId(id)
        }
}
