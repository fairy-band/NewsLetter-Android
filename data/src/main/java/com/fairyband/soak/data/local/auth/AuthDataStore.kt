package com.fairyband.soak.data.local.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AuthDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "auth data Store")
    private val dataStore = context.dataStore

    val userId: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    suspend fun setUserId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    val deviceToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[DEVICE_TOKEN].orEmpty()
    }

    suspend fun setDeviceToken(token: String) {
        dataStore.edit { preferences ->
            preferences[DEVICE_TOKEN] = token
        }
    }

    companion object {
        private val USER_ID = longPreferencesKey("user_id")
        private val DEVICE_TOKEN = stringPreferencesKey("device_token")
    }
}
