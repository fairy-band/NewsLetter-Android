package com.nexters.knownknowns.data.di

import com.nexters.knownknowns.data.di.qualifier.JWT
import com.nexters.knownknowns.data.remote.service.NewsLetterService
import com.nexters.knownknowns.data.remote.service.UserService
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
}
