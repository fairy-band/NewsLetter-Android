package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.data.local.ClickState
import com.nexters.knownknowns.data.local.DataStore
import com.nexters.knownknowns.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
internal class UserRepositoryImpl(
    private val dataStore: DataStore
) : UserRepository {
    override val clickStateFlow: Flow<ClickState>
        get() = dataStore.clickStateFlow

    override suspend fun incrementClickCount() {
        dataStore.incrementClickCount()
    }

    override suspend fun resetClickState() {
        dataStore.resetClickState()
    }

    override suspend fun recordBottomSheetShown() {
        dataStore.recordBottomSheetShown()
    }
}
