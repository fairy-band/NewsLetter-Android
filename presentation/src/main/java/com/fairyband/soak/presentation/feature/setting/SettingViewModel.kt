package com.fairyband.soak.presentation.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.UserRepository
import com.fairyband.soak.presentation.model.UserInfo
import com.fairyband.soak.presentation.model.toRequest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class SettingViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    fun saveUserInfo(
        preferences: List<String>,
        workingExperience: String
    ) {
        userRepository.putUserInfo(
            UserInfo(
                preferences = preferences,
                workingExperience = workingExperience
            ).toRequest()
        ).catch {
            Timber.e(it)
        }.launchIn(viewModelScope)
    }
}
