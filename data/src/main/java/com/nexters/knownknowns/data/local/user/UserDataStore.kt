package com.nexters.knownknowns.data.local.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

data class ClickState(
    val count: Int,
    val lastShownTimestamp: Long
)

@Single
class UserDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "user data Store")
    private val dataStore = context.dataStore

    val clickStateFlow: Flow<ClickState> = dataStore.data
        .map { preferences ->
            ClickState(
                count = preferences[CLICK_COUNT] ?: 0,
                lastShownTimestamp = preferences[LAST_SHOWN_TIMESTAMP] ?: 0L
            )
        }

    suspend fun recordBottomSheetShown() {
        dataStore.edit { preferences ->
            preferences[LAST_SHOWN_TIMESTAMP] = System.currentTimeMillis()
        }
    }

    suspend fun incrementClickCount() {
        dataStore.edit { preferences ->
            val current = preferences[CLICK_COUNT] ?: 0
            preferences[CLICK_COUNT] = current + 1
        }
    }

    suspend fun resetClickState() {
        dataStore.edit { preferences ->
            preferences[CLICK_COUNT] = 0
            preferences[LAST_SHOWN_TIMESTAMP] = 0L
        }
    }

    companion object {
        private val CLICK_COUNT = intPreferencesKey("news_click_count")
        private val LAST_SHOWN_TIMESTAMP = longPreferencesKey("last_shown_timestamp")
    }
}
