package com.nexters.knownknowns

import android.app.Application
import com.nexters.knownknowns.data.di.DataModule
import com.nexters.knownknowns.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class KnownKnownsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@KnownKnownsApplication)
            modules(DataModule().module, PresentationModule().module)
        }
    }
}
