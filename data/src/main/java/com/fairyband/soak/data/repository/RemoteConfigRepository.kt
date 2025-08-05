package com.fairyband.soak.data.repository

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    fun getCardColorType(): Flow<String>
}