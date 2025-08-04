package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.datasource.AuthDataSource
import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.RegisterResponse
import com.nexters.knownknowns.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override fun getUserId(): Flow<Long?> = authDataSource.getUserId()
    override suspend fun setUserId(id: Long) {
        authDataSource.setUserId(id)
    }

    override suspend fun registerUser(request: RegisterRequest): Flow<RegisterResponse> = flow {
        emit(authDataSource.registerUser(request = request))
    }
}
