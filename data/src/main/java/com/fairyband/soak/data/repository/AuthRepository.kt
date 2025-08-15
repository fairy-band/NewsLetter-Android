package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.response.LoginResponse

interface AuthRepository {
    suspend fun loginUser(): Result<LoginResponse>
}
