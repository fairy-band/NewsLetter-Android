package com.fairyband.soak.data.remote.service

import com.fairyband.soak.data.model.request.UserInfoRequest
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @PUT("api/users/{userId}")
    suspend fun putUserInfo(
        @Path(value = "userId") userId: Long,
        @Body body: UserInfoRequest,
    )
}
