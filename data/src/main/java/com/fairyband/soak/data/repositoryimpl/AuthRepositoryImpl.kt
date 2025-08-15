package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun loginUser(): Result<LoginResponse> =
        runCatching {
            authDataSource.loginUser()
        }
}
