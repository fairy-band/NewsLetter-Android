package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.NotificationDataSource
import com.fairyband.soak.data.datasource.UserDataSource
import com.fairyband.soak.data.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationDataSource: NotificationDataSource,
    private val userDataSource: UserDataSource,
) : NotificationRepository {
    override suspend fun registerFcmToken(fcmToken: String) {
        // TODO: deviceToken 제대로 된 거 넣어야 함
        notificationDataSource.registerFcmToken(deviceToken = "dummy", fcmToken = fcmToken)
    }
}