package com.nexters.knownknowns.data.local.user

import kotlinx.coroutines.flow.Flow

interface UserDataStore {
    val clickStateFlow: Flow<ClickState>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
}
