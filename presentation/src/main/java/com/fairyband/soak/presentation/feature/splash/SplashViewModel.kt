package com.fairyband.soak.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel(
    remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    // remoteConfig는 캐시되기 때문에 한 번 호출해 두면 이후에 호출할 땐 바로 가져올 것이다.
    val isLoaded = remoteConfigRepository.getCardColorType()
        .map { true }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false
        )
}