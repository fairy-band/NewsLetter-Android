package com.fairyband.soak.data.local.user

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

data class BottomSheetState(
    val isOnceShown: Boolean,
    val lastShownTimestamp: Long
)

@Single
class UserDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "user data Store")
    private val dataStore = context.dataStore

    val bottomSheetFlow: Flow<BottomSheetState> = dataStore.data
        .map { preferences ->
            BottomSheetState(
                isOnceShown = preferences[IS_ONCE_SHOWN] ?: false,
                lastShownTimestamp = preferences[LAST_SHOWN_TIMESTAMP] ?: 0L
            )
        }

    suspend fun recordBottomSheetShown() {
        dataStore.edit { preferences ->
            preferences[LAST_SHOWN_TIMESTAMP] = System.currentTimeMillis()
        }
    }

    suspend fun isOnceShown() {
        dataStore.edit { preferences ->
            preferences[IS_ONCE_SHOWN] = true
        }
    }

    suspend fun resetState() {
        dataStore.edit { preferences ->
            preferences[IS_ONCE_SHOWN] = false
            preferences[LAST_SHOWN_TIMESTAMP] = 0L
        }
    }

    companion object {
        private val IS_ONCE_SHOWN = booleanPreferencesKey("is_once_shown")
        private val LAST_SHOWN_TIMESTAMP = longPreferencesKey("last_shown_timestamp")
    }
}
