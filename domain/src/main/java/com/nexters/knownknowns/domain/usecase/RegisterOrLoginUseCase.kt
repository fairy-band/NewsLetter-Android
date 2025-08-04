package com.nexters.knownknowns.domain.usecase

import com.nexters.knownknowns.data.repository.AuthRepository
import com.nexters.knownknowns.domain.model.AuthInfo
import com.nexters.knownknowns.domain.model.toAuthInfo
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

        if (userId == null) registerUser()
        else loginUser()
    }

    private suspend fun registerUser() {
        val uuid = UUID.randomUUID().toString()

        authRepository.registerUser(
            AuthInfo(
                deviceToken = uuid
            ).toRequest()
        ).onSuccess { response ->
            val id = response.toAuthInfo().id

            authRepository.setUserId(id)
            authRepository.setDeviceToken(uuid) // TODO: 로그인 구현 시 deviceToken 로직은 지워주세요.
        }.onFailure(Timber::e)
    }

    private suspend fun loginUser() {
        val deviceToken = authRepository.getDeviceToken().first()

        authRepository.loginUser(
            deviceToken = deviceToken
        ).onSuccess { response ->
            val id = response.toAuthInfo().id

            authRepository.setUserId(id)
        }.onFailure(Timber::e)
    }
}
