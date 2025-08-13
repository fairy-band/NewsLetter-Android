package com.fairyband.soak.data.remote.service

import com.fairyband.soak.data.model.request.FcmRegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {
    @POST("api/notifications/token")
    suspend fun postNotificationToken(
        @Body request: FcmRegisterRequest,
    )
}