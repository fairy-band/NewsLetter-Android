package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.data.local.ClickState
import com.nexters.knownknowns.data.model.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<NewsResponse>>
    suspend fun incrementClickCount()
    suspend fun getClickState(): Flow<ClickState>
    suspend fun resetClickState()
    suspend fun recordBottomSheetShown()
}
