package com.fairyband.soak.data.repository

import com.fairyband.soak.data.local.user.ClickState
import com.fairyband.soak.data.model.request.UserInfoRequest
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface UserRepository {
    val clickStateFlow: Flow<ClickState>
    val streak: Flow<Int>
    val notificationEnabled: Flow<Boolean>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
    fun putUserInfo(request: UserInfoRequest): Flow<Unit>
    suspend fun disableNotificationSetting()
    suspend fun visitApp()
}
