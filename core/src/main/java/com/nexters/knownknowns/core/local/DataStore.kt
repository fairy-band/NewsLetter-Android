package com.nexters.knownknowns.core.local

import kotlinx.coroutines.flow.Flow

interface DataStore {
    val clickCountFlow: Flow<Int>
    suspend fun incrementClickCount()
    suspend fun resetClickCount()
}
