package com.nexters.knownknowns.data.datasource

import com.nexters.knownknowns.data.network.api.NewsLetterApi
import org.koin.core.annotation.Singleton

@Singleton
internal class NewsLetterDataSource(
    private val api: NewsLetterApi,
) {

}