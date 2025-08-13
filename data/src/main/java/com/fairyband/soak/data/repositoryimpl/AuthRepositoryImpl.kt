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
import timber.log.Timber
import timber.log.Timber.Forest.e

@Single
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override fun getUserId(): Flow<Long?> = authDataSource.getUserId()

    private suspend fun getDeviceToken(): String = authDataSource.getDeviceToken().first()

    override suspend fun registerUser(): Result<RegisterResponse> =
        runCatching {
            val request = RegisterRequest(deviceToken = getDeviceToken())
            authDataSource.registerUser(request)
        }.onFailure(Timber::e)

    override suspend fun loginUser(): Result<LoginResponse> =
        runCatching {
            authDataSource.loginUser(getDeviceToken())
        }.onFailure(Timber::e)
}
