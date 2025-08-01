package com.nexters.knownknowns.data.di

import com.nexters.knownknowns.data.di.qualifier.JWT
import com.nexters.knownknowns.data.remote.service.UserService
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit

@Module
class ServiceModule {
    @Single
    fun provideUserService(@JWT retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}
