package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.data.local.ClickState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val clickStateFlow: Flow<ClickState>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
}
