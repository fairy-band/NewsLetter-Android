package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.LoginResponse
import com.nexters.knownknowns.data.model.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getUserId(): Flow<Long?>
    suspend fun setUserId(id: Long)
    fun getDeviceToken(): Flow<String>
    suspend fun setDeviceToken(token: String)
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse>
    suspend fun loginUser(deviceToken: String): Result<LoginResponse>
}
