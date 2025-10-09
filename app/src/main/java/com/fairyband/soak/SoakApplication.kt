package com.fairyband.soak

import android.app.Application
import com.fairyband.soak.data.di.DataModule
import com.fairyband.soak.data.di.RetrofitModule
import com.fairyband.soak.data.di.ServiceModule
import com.fairyband.soak.domain.di.DomainModule
import com.fairyband.soak.presentation.di.PresentationModule
import com.google.firebase.installations.FirebaseInstallations
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class SoakApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SoakApplication)
            modules(
                DataModule().module,
                DomainModule().module,
                PresentationModule().module,
                RetrofitModule().module,
                ServiceModule().module
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        printInstallationToken()
        initKakaoSdk()
    }

    private fun printInstallationToken() {
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

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }
}
