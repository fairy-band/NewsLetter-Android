package com.fairyband.soak.presentation.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.NewsRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreViewModel(
    newsRepository: NewsRepository,
) : ViewModel() {
    val news = newsRepository.getExploreContents().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        persistentListOf()
    )
}