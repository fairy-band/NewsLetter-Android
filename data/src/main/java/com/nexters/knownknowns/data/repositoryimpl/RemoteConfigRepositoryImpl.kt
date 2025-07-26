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
                continuation.resume(null)
            }
        }
    }
}