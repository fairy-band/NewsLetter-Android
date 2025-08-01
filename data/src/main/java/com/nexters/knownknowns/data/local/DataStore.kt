package com.nexters.knownknowns.data.local

import kotlinx.coroutines.flow.Flow

// TODO: 네이밍 구체화 하기 by 이유빈
interface DataStore {
    val clickStateFlow: Flow<ClickState>
    suspend fun incrementClickCount()
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
}
