package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.data.local.user.ClickState
import com.nexters.knownknowns.data.model.request.UserInfoRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val clickStateFlow: Flow<ClickState>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
    suspend fun putUserInfo(request: UserInfoRequest): Result<Unit>
}
