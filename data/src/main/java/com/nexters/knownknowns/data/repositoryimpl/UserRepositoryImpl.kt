package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.datasource.AuthDataSource
import com.nexters.knownknowns.data.datasource.UserDataSource
import com.nexters.knownknowns.data.local.user.ClickState
import com.nexters.knownknowns.data.model.request.UserInfoRequest
import com.nexters.knownknowns.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
internal class UserRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override val clickStateFlow: Flow<ClickState> = userDataSource.clickStateFlow

    override suspend fun incrementClickCount() {
        userDataSource.incrementClickCount()
    }

    override suspend fun resetClickState() {
        userDataSource.resetClickState()
    }

    override suspend fun recordBottomSheetShown() {
        userDataSource.recordBottomSheetShown()
    }

    override suspend fun putUserInfo(request: UserInfoRequest): Flow<Unit> = flow {
        val userId = authDataSource.getUserId().first()

        emit(
            userDataSource.putUserInfo(
                userId = userId ?: 0L,
                request = request
            )
        )
    }
}
