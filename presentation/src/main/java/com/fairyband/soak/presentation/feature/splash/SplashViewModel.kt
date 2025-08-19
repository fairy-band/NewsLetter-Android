package com.fairyband.soak.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.AuthRepository
import com.fairyband.soak.data.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class SplashViewModel(
    remoteConfigRepository: RemoteConfigRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    // remoteConfig는 캐시되기 때문에 한 번 호출해 두면 이후에 호출할 땐 바로 가져올 것이다.
    val isLoaded = remoteConfigRepository.getCardColorType()
        .map { true }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )

    init {
        registerOrLogin()
    }

    private fun registerOrLogin() = viewModelScope.launch {
        authRepository.loginUser().onFailure(Timber::e)
    }
}