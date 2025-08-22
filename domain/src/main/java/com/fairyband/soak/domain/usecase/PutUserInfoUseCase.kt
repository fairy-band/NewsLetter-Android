package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.data.repository.UserRepository
import org.koin.core.annotation.Single

@Single
class PutUserInfoUseCase(
    private val userRepository: UserRepository,
    private val newsRepository: NewsRepository,
) {
    suspend operator fun invoke(request: UserInfoRequest) {
        userRepository.putUserInfo(request = request).collect { }
        newsRepository.invalidateNews()
    }
}