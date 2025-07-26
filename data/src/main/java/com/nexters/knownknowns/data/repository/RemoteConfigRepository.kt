package com.nexters.knownknowns.data.repository

import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {
    fun getColor(): Flow<String>
}