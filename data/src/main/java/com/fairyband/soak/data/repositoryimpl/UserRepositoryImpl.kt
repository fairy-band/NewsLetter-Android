package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.UserDataSource
import com.fairyband.soak.data.local.user.BottomSheetState
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
internal class UserRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override val bottomSheetFlow: Flow<BottomSheetState> = userDataSource.bottomSheetFlow
    override val streak: Flow<Int> = userDataSource.streak
    override val notificationEnabled: Flow<Boolean> =
        userDataSource.notificationSettingDateFlow.map { shownDate ->
            LocalDate.now() >= shownDate.plusDays(3)
        }

    override suspend fun resetState() {
        userDataSource.resetState()
    }

    override suspend fun recordBottomSheetShown() {
        userDataSource.recordBottomSheetShown()
    }

    override fun putUserInfo(request: UserInfoRequest): Flow<Unit> = flow {
        emit(
            userDataSource.putUserInfo(
                userId = authDataSource.getUserId(),
                request = request
            )
        )
    }

    override suspend fun disableNotificationSetting() {
        userDataSource.disableNotificationSetting()
    }

    override suspend fun visitApp() {
        userDataSource.visitApp()
    }
}
