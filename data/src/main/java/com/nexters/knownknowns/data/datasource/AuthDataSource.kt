package com.nexters.knownknowns.data.datasource

import com.nexters.knownknowns.data.local.auth.AuthDataStore
import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.LoginResponse
import com.nexters.knownknowns.data.model.response.RegisterResponse
import com.nexters.knownknowns.data.remote.service.AuthService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AuthDataSource(
    private val authDataStore: AuthDataStore,
    private val authService: AuthService,
) {
    fun getUserId(): Flow<Long?> = authDataStore.userId

    suspend fun setUserId(id: Long) {
        authDataStore.setUserId(id)
    }

    suspend fun registerUser(request: RegisterRequest): RegisterResponse =
        authService.registerUser(
            body = request
        )

    suspend fun loginUser(deviceToken: String): LoginResponse =
        authService.loginUser(
            deviceToken = deviceToken
        )
}
