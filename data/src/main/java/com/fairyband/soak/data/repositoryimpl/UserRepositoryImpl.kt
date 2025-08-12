package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.UserDataSource
import com.fairyband.soak.data.local.user.ClickState
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
internal class UserRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override val clickStateFlow: Flow<ClickState> = userDataSource.clickStateFlow
    override val shouldShowNotificationSetting: Flow<Boolean> = userDataSource
        .streak
        .combine(userDataSource.notificationSettingDateFlow) { streak, shownDate ->
            streak >= 2 && LocalDate.now() >= shownDate.plusDays(3)
        }

    override suspend fun incrementClickCount() {
        userDataSource.incrementClickCount()
    }

    override suspend fun resetClickState() {
        userDataSource.resetClickState()
    }

    override suspend fun recordBottomSheetShown() {
        userDataSource.recordBottomSheetShown()
    }

    override fun putUserInfo(request: UserInfoRequest): Flow<Unit> =
        authDataSource.getUserId().map { userId ->
            userDataSource.putUserInfo(
                userId = userId ?: 0L,
                request = request
            )
        }

    override suspend fun disableNotificationSetting() {
        userDataSource.disableNotificationSetting()
    }

    override suspend fun visitApp() {
        userDataSource.visitApp()
    }
}
