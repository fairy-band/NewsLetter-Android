package com.nexters.knownknowns.domain.usecase

import com.nexters.knownknowns.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Single
class BottomSheetUseCase(
    private val userRepository: UserRepository,
) {
    val shouldShowBottomSheet: Flow<Boolean> = userRepository.clickStateFlow
        .map { clickState ->
            val lastShown = clickState.lastShownTimestamp

            if (lastShown > 0L) {
                val sevenDaysInMillis = TimeUnit.DAYS.toMillis(SUPPRESSION_DAYS)
                val timeSinceLastShown = System.currentTimeMillis() - lastShown

                if (timeSinceLastShown > sevenDaysInMillis) userRepository.resetClickState()
                return@map false

            } else {
                clickState.count == TRIGGER_COUNT
            }
        }
        .distinctUntilChanged()

    suspend fun onNewsClicked() {
        userRepository.incrementClickCount()
    }

    companion object {
        private const val SUPPRESSION_DAYS = 7L
        private const val TRIGGER_COUNT = 3
    }
}
