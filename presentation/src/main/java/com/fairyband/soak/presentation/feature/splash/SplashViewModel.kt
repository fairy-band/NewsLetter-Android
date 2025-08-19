package com.fairyband.soak.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.AuthRepository
import com.fairyband.soak.data.repository.RemoteConfigRepository
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class SplashViewModel(
    private val remoteConfigRepository: RemoteConfigRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _shouldGoHome = MutableStateFlow(false)
    val shouldGoHome = _shouldGoHome.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        val login = async {
            registerOrLogin()
        }
        val fetchRemoteConfig = async {
            fetchRemoteConfigs()
        }

        listOf(login, fetchRemoteConfig).joinAll()

        _shouldGoHome.update { true }
    }

    private suspend fun registerOrLogin() {
        authRepository.loginUser()
            .onSuccess {}
            .onFailure {
                Timber.e(it)
                Firebase.crashlytics.recordException(it)
            }
    }

    // remoteConfig는 캐시되기 때문에 한 번 호출해 두면 이후에 호출할 땐 바로 가져올 것이다.
    private suspend fun fetchRemoteConfigs() {
        remoteConfigRepository.getCardColorType().first()
    }
}