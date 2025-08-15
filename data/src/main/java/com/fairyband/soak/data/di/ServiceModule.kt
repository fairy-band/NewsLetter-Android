package com.fairyband.soak.data.di

import com.fairyband.soak.data.di.qualifier.JWT
import com.fairyband.soak.data.remote.service.AuthService
import com.fairyband.soak.data.remote.service.NewsLetterService
import com.fairyband.soak.data.remote.service.NotificationService
import com.fairyband.soak.data.remote.service.UserService
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.create

@Module
class ServiceModule {
    @Single
    fun provideUserService(@JWT retrofit: Retrofit): UserService = retrofit.create()

    @Single
    fun provideNewsLetterService(@JWT retrofit: Retrofit): NewsLetterService = retrofit.create()

    @Single
    fun provideAuthService(@JWT retrofit: Retrofit): AuthService = retrofit.create()

    @Single
    fun provideNotificationService(@JWT retrofit: Retrofit): NotificationService = retrofit.create()
}
