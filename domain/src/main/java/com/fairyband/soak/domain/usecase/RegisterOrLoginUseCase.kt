package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.repository.AuthRepository
import com.fairyband.soak.domain.model.AuthInfo
import com.fairyband.soak.domain.model.toAuthInfo
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single
import timber.log.Timber
import java.util.UUID

@Single
class RegisterOrLoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val userId = authRepository.getUserId().first()

        if (userId == null) authRepository.registerUser()
        else authRepository.loginUser()
    }
}
