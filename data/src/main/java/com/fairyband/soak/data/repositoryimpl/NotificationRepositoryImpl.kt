package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NotificationDataSource
import com.fairyband.soak.data.repository.NotificationRepository
import org.koin.core.annotation.Single

@Single
class NotificationRepositoryImpl(
    private val notificationDataSource: NotificationDataSource,
    private val authDataSource: AuthDataSource,
) : NotificationRepository {
    override suspend fun registerFcmToken(fcmToken: String) {
        val deviceToken = authDataSource.getDeviceToken()
        notificationDataSource.registerFcmToken(deviceToken = deviceToken, fcmToken = fcmToken)
    }
}