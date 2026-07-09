package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.model.response.UserInfoResponse
import com.fairyband.soak.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetUserInfoUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<UserInfoResponse> = userRepository.getUserInfo()
}
