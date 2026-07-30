package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.user.UserDataStore
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.model.response.UserInfoResponse
import com.fairyband.soak.data.remote.service.UserService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class UserDataSource(
    private val userDataStore: UserDataStore,
    private val userService: UserService,
) {
    val streak: Flow<Int> = userDataStore.streakFlow
    val notificationSettingDateFlow: Flow<LocalDate> = userDataStore.notificationSettingDateFlow

    suspend fun getUserInfo(userId: Long): UserInfoResponse =
        userService.getUserInfo(userId = userId)

    suspend fun putUserInfo(
        userId: Long,
        request: UserInfoRequest
    ) {
        userService.putUserInfo(
            userId = userId,
            body = request
        )
    }

    suspend fun visitApp() {
        userDataStore.updateLastVisitedDate()
    }

    suspend fun disableNotificationSetting() {
        userDataStore.updateLastNotificationSettingDate()
    }
}
