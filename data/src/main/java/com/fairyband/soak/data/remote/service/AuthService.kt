package com.fairyband.soak.data.remote.service

import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/users/register")
    suspend fun registerUser(
        @Body body: RegisterRequest
    ): LoginResponse

    @GET("api/users/login")
    suspend fun loginUser(
        @Query("deviceToken") deviceToken: String
    ): LoginResponse
}
