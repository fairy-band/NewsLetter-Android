package com.nexters.knownknowns.domain.usecase

import com.nexters.knownknowns.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Single
class BottomSheetUseCase(
    private val newsRepository: NewsRepository
) {
    val shouldShowBottomSheetFlow: Flow<Boolean> = newsRepository.clickStateFlow
        .map { clickState ->
            val lastShown = clickState.lastShownTimestamp

            if (lastShown > 0L) {
                val sevenDaysInMillis = TimeUnit.DAYS.toMillis(SUPPRESSION_DAYS)
                val timeSinceLastShown = System.currentTimeMillis() - lastShown

                if (timeSinceLastShown > sevenDaysInMillis) newsRepository.resetClickState()
                return@map false

            } else {
                clickState.count >= TRIGGER_COUNT
            }
        }
        .distinctUntilChanged()

    suspend fun onNewsClicked() {
        newsRepository.incrementClickCount()
    }

    companion object {
        private const val SUPPRESSION_DAYS = 7L
        private const val TRIGGER_COUNT = 3
    }
}
