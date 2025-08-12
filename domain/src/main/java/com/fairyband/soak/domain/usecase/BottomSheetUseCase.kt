package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Single
class BottomSheetUseCase(
    private val userRepository: UserRepository,
) {
    val shouldShowBottomSheet: Flow<Boolean> = userRepository.bottomSheetFlow
        .map { clickState ->
            val lastShown = clickState.lastShownTimestamp

            if (lastShown > 0L) {
                val sevenDaysInMillis = TimeUnit.DAYS.toMillis(SUPPRESSION_DAYS)
                val timeSinceLastShown = System.currentTimeMillis() - lastShown

                if (timeSinceLastShown > sevenDaysInMillis) userRepository.resetClickState()

                return@map false

            } else {
                !clickState.isOnceShown
            }
        }
        .distinctUntilChanged()

    suspend fun onBottomSheetButtonClick() {
        userRepository.isOnceShown()
    }

    companion object {
        private const val SUPPRESSION_DAYS = 7L
    }
}
