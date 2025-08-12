package com.fairyband.soak.data.repository

import com.fairyband.soak.data.local.user.ClickState
import com.fairyband.soak.data.model.request.UserInfoRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val clickStateFlow: Flow<ClickState>
    val shouldShowNotificationSetting: Flow<Boolean>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
    fun putUserInfo(request: UserInfoRequest): Flow<Unit>
    suspend fun disableNotificationSetting()
    suspend fun visitApp()
}
