package com.nexters.knownknowns.data.repository

import com.nexters.knownknowns.data.model.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<NewsResponse>>
}