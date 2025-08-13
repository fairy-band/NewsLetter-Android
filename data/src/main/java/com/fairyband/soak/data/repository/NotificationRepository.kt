package com.fairyband.soak.data.repository

interface NotificationRepository {
    suspend fun registerFcmToken(fcmToken: String)
}