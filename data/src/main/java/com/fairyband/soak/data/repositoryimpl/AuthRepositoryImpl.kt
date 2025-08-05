package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse
import com.fairyband.soak.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override fun getUserId(): Flow<Long?> = authDataSource.getUserId()

    override suspend fun setUserId(id: Long) {
        authDataSource.setUserId(id)
    }

    override fun getDeviceToken(): Flow<String> = authDataSource.getDeviceToken()

    override suspend fun setDeviceToken(token: String) {
        authDataSource.setDeviceToken(token)
    }

    override suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> =
        runCatching {
            authDataSource.registerUser(request)
        }

    override suspend fun loginUser(deviceToken: String): Result<LoginResponse> =
        runCatching {
            authDataSource.loginUser(deviceToken)
        }
}
