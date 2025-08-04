package com.nexters.knownknowns.data.local.auth

import kotlinx.coroutines.flow.Flow

interface AuthDataStore {
    val userId: Flow<Long?>
    suspend fun setUserId(userId: Long)
}
