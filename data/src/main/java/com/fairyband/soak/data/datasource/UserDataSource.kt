package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.user.BottomSheetState
import com.fairyband.soak.data.local.user.UserDataStore
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.remote.service.UserService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class UserDataSource(
    private val userDataStore: UserDataStore,
    private val userService: UserService,
) {
    val streak: Flow<Int>
        get() = userDataStore.streakFlow

    val notificationSettingDateFlow: Flow<LocalDate>
        get() = userDataStore.notificationSettingDateFlow

    suspend fun putUserInfo(
        userId: Long,
        request: UserInfoRequest
    ) {
        userService.putUserInfo(
            userId = userId,
            body = request
        )
    }

    val bottomSheetFlow: Flow<BottomSheetState>
        get() = userDataStore.bottomSheetFlow

    suspend fun resetState() {
        userDataStore.resetState()
    }

    suspend fun recordBottomSheetShown() {
        userDataStore.recordBottomSheetShown()
    }

    suspend fun visitApp() {
        userDataStore.updateLastVisitedDate()
    }

    suspend fun disableNotificationSetting() {
        userDataStore.updateLastNotificationSettingDate()
    }
}
