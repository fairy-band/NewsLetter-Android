package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.abtest.HomeTitleVariant
import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    fun getCardColorType(): Flow<String>
    fun getHomeTitleVariant(): Flow<HomeTitleVariant>
    fun getRequiredVersion(): Flow<String?>
}