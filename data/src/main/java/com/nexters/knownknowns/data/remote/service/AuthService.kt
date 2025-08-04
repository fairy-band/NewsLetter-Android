package com.nexters.knownknowns.data.remote.service

import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/users/register")
    suspend fun registerUser(
        @Body body: RegisterRequest
    ): RegisterResponse
}
