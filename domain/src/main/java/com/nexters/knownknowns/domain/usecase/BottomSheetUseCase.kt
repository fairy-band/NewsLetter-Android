package com.nexters.knownknowns.domain.usecase

import com.nexters.knownknowns.data.repository.NewsRepository
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class BottomSheetUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Boolean {
        val currentState = newsRepository.getClickState().first()
        val lastShown = currentState.lastShownTimestamp

        if (lastShown > 0L) {
            val sevenDaysInMillis = TimeUnit.DAYS.toMillis(SUPPRESSION_DAYS)
            val timeSinceLastShown = System.currentTimeMillis() - lastShown

            if (timeSinceLastShown < sevenDaysInMillis) return false

            newsRepository.resetClickState()
            return false
        } else {
            newsRepository.incrementClickCount()

            return currentState.count == TRIGGER_COUNT - 1
        }
    }

    companion object {
        private const val SUPPRESSION_DAYS = 7L
        private const val TRIGGER_COUNT = 3
    }
}
