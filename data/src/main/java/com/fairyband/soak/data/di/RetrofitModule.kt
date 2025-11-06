package com.fairyband.soak.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.fairyband.soak.core.extension.isJsonArray
import com.fairyband.soak.core.extension.isJsonObject
import com.fairyband.soak.data.BuildConfig.BASE_URL
import com.fairyband.soak.data.di.qualifier.JWT
import com.fairyband.soak.data.di.qualifier.Locale
import com.fairyband.soak.data.remote.AuthInterceptor
import com.fairyband.soak.data.remote.LocaleInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber

@Module
class RetrofitModule {
    @Single
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Single
    fun provideJsonConverter(json: Json): Converter.Factory =
        json.asConverterFactory(APPLICATION_JSON.toMediaType())

    @Single
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor { message ->
        when {
            message.isJsonObject() ->
                Timber.tag("okhttp").d(JSONObject(message).toString(4))

            message.isJsonArray() ->
                Timber.tag("okhttp").d(JSONObject(message).toString(4))

            else -> {
                Timber.tag("okhttp").d("CONNECTION INFO -> $message")
            }
        }
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Single
    @JWT
    fun provideAuthInterceptor(authInterceptor: AuthInterceptor): Interceptor = authInterceptor

    @Single
    @Locale
    fun provideLocaleInterceptor(localeInterceptor: LocaleInterceptor): Interceptor = localeInterceptor

    @Single
    @JWT
    fun provideJWTOkHttpClient(
        loggingInterceptor: Interceptor,
        @JWT authInterceptor: Interceptor,
        @Locale localeInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(localeInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    @Single
    @JWT
    fun provideJWTRetrofit(
        @JWT client: OkHttpClient,
        factory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(factory)
        .build()

    companion object {
        private const val APPLICATION_JSON = "application/json"
    }
}
