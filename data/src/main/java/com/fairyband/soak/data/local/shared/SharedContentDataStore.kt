package com.fairyband.soak.data.local.shared

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fairyband.soak.data.model.local.SharedContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class SharedContentDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "shared_content_data_store")
    private val dataStore = context.dataStore
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 최근에 공유받은 순으로 정렬돼 있어요.
     */
    val sharedContents: Flow<List<SharedContent>> = dataStore.data
        .map { preferences -> decode(preferences[SHARED_CONTENTS]) }

    /**
     * 같은 콘텐츠를 다시 공유받으면 기존 항목을 지우고 맨 앞에 추가해요.
     */
    suspend fun save(content: SharedContent) {
        dataStore.edit { preferences ->
            val contents = decode(preferences[SHARED_CONTENTS])
                .filterNot { it.exposureContentId == content.exposureContentId }
            preferences[SHARED_CONTENTS] = json.encodeToString(
                (listOf(content) + contents).take(MAX_SIZE)
            )
        }
    }

    private fun decode(raw: String?): List<SharedContent> {
        if (raw == null) return emptyList()
        return runCatching { json.decodeFromString<List<SharedContent>>(raw) }
            .onFailure { Timber.e(it, "공유받은 콘텐츠 디코딩 실패") }
            .getOrDefault(emptyList())
    }

    companion object {
        private const val MAX_SIZE = 100
        private val SHARED_CONTENTS = stringPreferencesKey("shared_contents")
    }
}
