package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.local.user.UserDataStore
import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse
import com.fairyband.soak.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override fun getUserId(): Flow<Long?> = authDataSource.getUserId()

    override suspend fun setUserId(id: Long) {
        authDataSource.setUserId(id)
    }

    override fun getDeviceToken(): Flow<String> = authDataSource.getDeviceToken()

    override suspend fun registerUser(): Result<RegisterResponse> =
        runCatching {
            val deviceToken = getDeviceToken().first()
            val request = RegisterRequest(deviceToken = deviceToken)
            authDataSource.registerUser(request)
        }

    override suspend fun loginUser(deviceToken: String): Result<LoginResponse> =
        runCatching {
            authDataSource.loginUser(deviceToken)
        }
}
