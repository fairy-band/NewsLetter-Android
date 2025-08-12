package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.user.ClickState
import com.fairyband.soak.data.local.user.UserDataStore
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.remote.service.UserService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
internal class UserDataSource(
    private val userDataStore: UserDataStore,
    private val userService: UserService,
) {
    val clickStateFlow: Flow<ClickState>
        get() = userDataStore.clickStateFlow

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

    suspend fun incrementClickCount() {
        userDataStore.incrementClickCount()
    }

    suspend fun resetClickState() {
        userDataStore.resetClickState()
    }

    suspend fun recordBottomSheetShown() {
        userDataStore.recordBottomSheetShown()
    }

    suspend fun visitApp() {
        userDataStore.updateLastVisitedDate()
    }
}
