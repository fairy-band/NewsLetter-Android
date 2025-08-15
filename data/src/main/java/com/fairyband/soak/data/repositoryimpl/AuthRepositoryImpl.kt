package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse
import com.fairyband.soak.data.repository.AuthRepository
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun getUserId(): Long = authDataSource.getUserId()

    private suspend fun getDeviceToken(): String = authDataSource.getDeviceToken()

    override suspend fun registerUser(): Result<RegisterResponse> =
        runCatching {
            val request = RegisterRequest(deviceToken = getDeviceToken())
            authDataSource.registerUser(request)
        }.onFailure(Timber::e)

    override suspend fun loginUser(): Result<LoginResponse> =
        runCatching {
            authDataSource.loginUser()
        }.onFailure(Timber::e)
}
