package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.local.SharedContent
import kotlinx.coroutines.flow.Flow

interface SharedContentRepository {
    /**
     * 최근에 공유받은 순으로 정렬돼 있어요.
     */
    val sharedContents: Flow<List<SharedContent>>

    suspend fun saveSharedContent(content: SharedContent)
}
