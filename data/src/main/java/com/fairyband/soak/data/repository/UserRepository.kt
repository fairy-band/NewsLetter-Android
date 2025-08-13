package com.fairyband.soak.data.repository

import com.fairyband.soak.data.local.user.BottomSheetState
import com.fairyband.soak.data.model.request.UserInfoRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val bottomSheetFlow: Flow<BottomSheetState>
    suspend fun resetState()
    val streak: Flow<Int>
    val notificationEnabled: Flow<Boolean>
    suspend fun recordBottomSheetShown()
    fun putUserInfo(request: UserInfoRequest): Flow<Unit>
    suspend fun disableNotificationSetting()
    suspend fun visitApp()
}
