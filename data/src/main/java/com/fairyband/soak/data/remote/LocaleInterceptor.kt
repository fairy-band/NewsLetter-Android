package com.fairyband.soak.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single
import java.util.Locale

@Single
class LocaleInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val locale = getLocaleHeader()

        val request = chain.request().newBuilder()
            .addHeader("X-Locale", locale)
            .build()

        return chain.proceed(request)
    }

    private fun getLocaleHeader(): String {
        val systemLocale = Locale.getDefault()
        return when (systemLocale.language) {
            "en" -> "en-US"
            "ko" -> "ko-KR"
            else -> "ko-KR"
        }
    }
}