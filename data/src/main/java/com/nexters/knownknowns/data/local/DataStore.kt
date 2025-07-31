package com.nexters.knownknowns.data.local

import kotlinx.coroutines.flow.Flow

interface DataStore {
    val clickStateFlow: Flow<ClickState>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
}
