package com.nexters.knownknowns.core.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class DataStoreImpl(context: Context) : DataStore {
    private val Context.dataStore by preferencesDataStore(name = "dataStore")
    private val dataStore = context.dataStore

    override val clickCountFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[CLICK_COUNT] ?: 0
        }

    override suspend fun incrementClickCount() {
        dataStore.edit { preferences ->
            val current = preferences[CLICK_COUNT] ?: 0
            preferences[CLICK_COUNT] = current + 1
        }
    }

    override suspend fun resetClickCount() {
        dataStore.edit { preferences ->
            preferences[CLICK_COUNT] = 0
        }
    }

    companion object {
        private val CLICK_COUNT = intPreferencesKey("news_click_count")
    }
}
