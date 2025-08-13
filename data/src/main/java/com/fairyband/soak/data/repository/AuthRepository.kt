package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.model.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUserId(): Long
    suspend fun registerUser(): Result<RegisterResponse>
    suspend fun loginUser(): Result<LoginResponse>
}
