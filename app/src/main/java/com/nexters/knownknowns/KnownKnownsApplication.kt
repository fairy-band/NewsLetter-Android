package com.nexters.knownknowns

import android.app.Application
import com.google.firebase.installations.FirebaseInstallations
import com.nexters.knownknowns.core.local.di.DataStoreModule
import com.nexters.knownknowns.data.di.DataModule
import com.nexters.knownknowns.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class KnownKnownsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@KnownKnownsApplication)
            modules(
                DataModule().module,
                PresentationModule().module,
                DataStoreModule().module,
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        printInstallationToken()
    }

    fun printInstallationToken() {
        if (!BuildConfig.DEBUG) return

        FirebaseInstallations.getInstance().getToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("설치 인증 토큰: ${task.result?.token}")
                } else {
                    Timber.d("AB 테스트를 위한 파이어베이스 설치 토큰을 받지 못했어요.")
                }
            }
    }
}
