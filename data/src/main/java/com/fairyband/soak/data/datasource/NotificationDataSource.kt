package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.model.request.FcmRegisterRequest
import com.fairyband.soak.data.remote.service.NotificationService
import org.koin.core.annotation.Single

@Single
class NotificationDataSource(
    private val notificationService: NotificationService
) {
    suspend fun registerFcmToken(
        deviceToken: String,
        fcmToken: String,
    ) {
        notificationService.postNotificationToken(
            request = FcmRegisterRequest(
                deviceToken = deviceToken,
                fcmToken = fcmToken,
                deviceType = "ANDROID"
            )
        )
    }
}