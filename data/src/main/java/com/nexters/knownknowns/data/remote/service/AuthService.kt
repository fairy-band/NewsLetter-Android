package com.nexters.knownknowns.data.remote.service

import com.nexters.knownknowns.data.model.request.RegisterRequest
import com.nexters.knownknowns.data.model.response.LoginResponse
import com.nexters.knownknowns.data.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/users/register")
    suspend fun registerUser(
        @Body body: RegisterRequest
    ): RegisterResponse

    @GET("api/users/login")
    suspend fun loginUser(
        @Query("deviceToken") deviceToken: String
    ): LoginResponse
}
