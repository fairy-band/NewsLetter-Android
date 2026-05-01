package com.fairyband.soak.data.local.news

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class NewsDataStore(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "news_data_store")
    private val dataStore = context.dataStore

    val hasRefreshedToday: Flow<Boolean> = dataStore.data
        .map { preferences ->
            val lastRefreshDay = preferences[LAST_REFRESH_DATE] ?: -1L
            lastRefreshDay == LocalDate.now().toEpochDay()
        }

    suspend fun recordRefreshToday() {
        dataStore.edit { preferences ->
            preferences[LAST_REFRESH_DATE] = LocalDate.now().toEpochDay()
        }
    }

    companion object {
        private val LAST_REFRESH_DATE = longPreferencesKey("last_refresh_date")
    }
}
