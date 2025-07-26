package com.nexters.knownknowns

import android.app.Application
import com.google.firebase.BuildConfig
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
            modules(DataModule().module, PresentationModule().module)
        }

        // fixme build config import 억까 당해서 일단 파베 패키지 임포트 해서 사용 중
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
