package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.user.ClickState
import com.fairyband.soak.data.local.user.UserDataStore
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.remote.service.UserService
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
internal class UserDataSource(
    private val userDataStore: UserDataStore,
    private val userService: UserService,
) {
    suspend fun putUserInfo(
        userId: Long,
        request: UserInfoRequest
    ) {
        userService.putUserInfo(
            userId = userId,
            body = request
        )
    }

    val clickStateFlow: Flow<ClickState>
        get() = userDataStore.clickStateFlow

    suspend fun isOnceShown() {
        userDataStore.isOnceShown()
    }

    suspend fun resetClickState() {
        userDataStore.resetClickState()
    }

    suspend fun recordBottomSheetShown() {
        userDataStore.recordBottomSheetShown()
    }
}
