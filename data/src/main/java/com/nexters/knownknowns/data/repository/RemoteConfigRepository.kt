package com.nexters.knownknowns.data.repository

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    fun getCardColorType(): Flow<String>
}