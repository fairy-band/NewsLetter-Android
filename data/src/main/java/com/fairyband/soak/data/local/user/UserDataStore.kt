package com.fairyband.soak.data.local.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

data class BottomSheetState(
    val lastShownTimestamp: Long
)

@Single
class UserDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "user data Store")
    private val dataStore = context.dataStore

    val bottomSheetFlow: Flow<BottomSheetState> = dataStore.data
        .map { preferences ->
            BottomSheetState(
                lastShownTimestamp = preferences[LAST_SHOWN_TIMESTAMP] ?: 0L
            )
        }

    suspend fun recordBottomSheetShown() {
        dataStore.edit { preferences ->
            preferences[LAST_SHOWN_TIMESTAMP] = System.currentTimeMillis()
        }
    }

    suspend fun resetState() {
        dataStore.edit { preferences ->
            preferences[LAST_SHOWN_TIMESTAMP] = 0L
        }
    }

    companion object {
        private val LAST_SHOWN_TIMESTAMP = longPreferencesKey("last_shown_timestamp")
    }
}
