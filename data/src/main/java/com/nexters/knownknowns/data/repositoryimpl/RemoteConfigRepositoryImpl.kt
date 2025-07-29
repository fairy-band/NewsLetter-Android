package com.nexters.knownknowns.data.repositoryimpl

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.nexters.knownknowns.data.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * AB테스트를 위해 Remote Config 설정을 가져옵니다. 아래 코드는 예시이므로,
 * 실제 사용할 때에는 새롭게 AB테스트를 설정 후 아래 코드를 수정하여 사용하세요.
 */
@Single
internal class RemoteConfigRepositoryImpl : RemoteConfigRepository {
    private val remoteConfig = Firebase.remoteConfig

    override fun getColor(): Flow<String> = flow {
        emit(getString("color") ?: "static")
    }

    private suspend fun getString(key: String): String? = suspendCoroutine { continuation ->
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig[key].asString().let {
                    Timber.d("remoteConfig[${key}]=${it}")
                    continuation.resume(it)
                }
            } else {
                Timber.d("remoteConfig[${key}] 값을 가져오는 데 실패했어요.")
                continuation.resume(null)
            }
        }
    }
}