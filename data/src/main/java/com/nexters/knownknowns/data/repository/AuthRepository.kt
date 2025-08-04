package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getUserId(): Flow<Long?>
    suspend fun setUserId(id: Long)
    suspend fun registerUser(request: RegisterRequest) : Flow<RegisterResponse>
}
