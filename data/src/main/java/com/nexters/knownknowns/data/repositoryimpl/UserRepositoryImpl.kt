package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.datasource.UserDataSource
import com.nexters.knownknowns.data.local.auth.AuthDataStore
import com.nexters.knownknowns.data.local.user.ClickState
import com.nexters.knownknowns.data.local.user.UserDataStore
import com.nexters.knownknowns.data.model.request.UserInfoRequest
import com.nexters.knownknowns.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
internal class UserRepositoryImpl(
    private val userDataStore: UserDataStore,
    private val userDataSource: UserDataSource,
    private val authDataStore: AuthDataStore
) : UserRepository {
    override val clickStateFlow: Flow<ClickState>
        get() = userDataStore.clickStateFlow

    override suspend fun incrementClickCount() {
        userDataStore.incrementClickCount()
    }

    override suspend fun resetClickState() {
        userDataStore.resetClickState()
    }

    override suspend fun recordBottomSheetShown() {
        userDataStore.recordBottomSheetShown()
    }

    override suspend fun putUserInfo(request: UserInfoRequest): Result<Unit> = runCatching {
        val userId = authDataStore.userId.first()

        userDataSource.putUserInfo(
            userId = userId ?: 0L,
            request = request
        )
    }
}
