package com.nexters.knownknowns.data.datasource

import com.nexters.knownknowns.data.local.user.ClickState
import com.nexters.knownknowns.data.local.user.UserDataStore
import com.nexters.knownknowns.data.model.request.UserInfoRequest
import com.nexters.knownknowns.data.remote.service.UserService
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

    suspend fun incrementClickCount() {
        userDataStore.incrementClickCount()
    }

    suspend fun resetClickState() {
        userDataStore.resetClickState()
    }

    suspend fun recordBottomSheetShown() {
        userDataStore.recordBottomSheetShown()
    }
}
