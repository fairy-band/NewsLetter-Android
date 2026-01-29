package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.BuildConfig
import com.fairyband.soak.data.model.abtest.HomeTitleVariant
import com.fairyband.soak.data.repository.RemoteConfigRepository
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Single
internal class RemoteConfigRepositoryImpl : RemoteConfigRepository {
    private val remoteConfig = Firebase.remoteConfig

    init {
        if (BuildConfig.DEBUG) {
            remoteConfig.setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0
                }
            )
        }
    }

    override fun getCardColorType(): Flow<String> = flow {
        emit(getString("card_color") ?: "B")
    }

    override fun getHomeTitleVariant(): Flow<HomeTitleVariant> = flow {
        val variantString = getString("main_description")
        val variant = if (variantString == "new") {
            HomeTitleVariant.NEW
        } else {
            HomeTitleVariant.EXISTING
        }

        emit(variant)
    }

    override fun getRequiredVersion(): Flow<String?> = flow {
        emit(getString("required_version"))
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