package com.nexters.knownknowns.core.local

import kotlinx.coroutines.flow.Flow

interface DataStore {
    val clickStateFlow: Flow<ClickState>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
     fun getClickState(): Flow<ClickState>
    suspend fun recordBottomSheetShown()
}
