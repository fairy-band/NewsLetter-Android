package com.fairyband.soak.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.AuthRepository
import com.fairyband.soak.data.repository.RemoteConfigRepository
import com.fairyband.soak.presentation.BuildConfig
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    private val _shouldUpdate = MutableStateFlow(false)
    val shouldUpdate = _shouldUpdate.asStateFlow()

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

        listOf(login, fetchRemoteConfig).awaitAll()

        if (_shouldUpdate.value) {
            return@launch
        }

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

    private suspend fun fetchRemoteConfigs() {
        remoteConfigRepository.getCardColorType().first() // 홈 화면에서 캐시 값을 사용하기 위해 부르기만 한 것으로 보임
        checkRequiredVersion()
    }

    private suspend fun checkRequiredVersion() {
        val requiredVersion = remoteConfigRepository.getRequiredVersion().first() ?: return
        val currentVersion = BuildConfig.VERSION_NAME

        if (isOldVersion(requiredVersion, currentVersion)) {
            _shouldUpdate.value = true
        }
    }

    private fun isOldVersion(requiredVersion: String, currentVersion: String): Boolean {
        var innerCurrentVersion = currentVersion
        if (currentVersion.startsWith("v")) {
            innerCurrentVersion = currentVersion.substring(1)
        }

        val requiredParts = requiredVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = innerCurrentVersion.split(".").map { it.toIntOrNull() ?: 0 }

        repeat(3) { index ->
            val required = requiredParts[index]
            val current = currentParts[index]

            if (required != current) {
                return required > current
            }
        }

        return false
    }
}