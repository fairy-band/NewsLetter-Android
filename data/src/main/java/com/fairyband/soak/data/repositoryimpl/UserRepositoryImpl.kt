package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.UserDataSource
import com.fairyband.soak.data.local.user.BottomSheetState
import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
internal class UserRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override val bottomSheetFlow: Flow<BottomSheetState> = userDataSource.bottomSheetFlow

    override suspend fun setAsShownOnce() {
        userDataSource.setAsShownOnce()
    }

    override suspend fun resetState() {
        userDataSource.resetState()
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
}
