package com.fairyband.soak.data.local.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.time.LocalDate

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

    val streakFlow: Flow<Int> = dataStore.data
        .map { preferences -> preferences[STREAK] ?: 0 }

    val notificationSettingDateFlow: Flow<LocalDate> = dataStore.data
        .map { preferences ->
            val epochDate = preferences[NOTIFICATION_SHOWN_DATE] ?: LocalDate.MIN.toEpochDay()
            LocalDate.ofEpochDay(epochDate)
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

    /**
     * 마지막 방문 날짜를 업데이트하고 연속 일수를 기록해요.
     */
    suspend fun updateLastVisitedDate() {
        dataStore.edit { preferences ->
            if (preferences[LAST_OPEN_DATE] == LocalDate.now().toEpochDay()) return@edit

            preferences[LAST_OPEN_DATE] = LocalDate.now().toEpochDay()
            preferences[STREAK] = (preferences[STREAK] ?: 0) + 1
        }
    }

    suspend fun updateLastNotificationSettingDate() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_SHOWN_DATE] = LocalDate.now().toEpochDay()
        }
    }

    companion object {
        private val CLICK_COUNT = intPreferencesKey("news_click_count")
        private val LAST_SHOWN_TIMESTAMP = longPreferencesKey("last_shown_timestamp")
        private val LAST_OPEN_DATE = longPreferencesKey("last_open_date")
        private val NOTIFICATION_SHOWN_DATE = longPreferencesKey("notification_shown_date")
        private val STREAK = intPreferencesKey("streak")
    }
}
