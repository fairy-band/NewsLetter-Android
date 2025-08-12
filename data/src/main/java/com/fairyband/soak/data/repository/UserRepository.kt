package com.fairyband.soak.data.repository

import com.fairyband.soak.data.local.user.ClickState
import com.fairyband.soak.data.model.request.UserInfoRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val clickStateFlow: Flow<ClickState>
    suspend fun isOnceShown()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
    fun putUserInfo(request: UserInfoRequest): Flow<Unit>
}
