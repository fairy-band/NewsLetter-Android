package com.fairyband.soak.presentation.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import com.fairyband.soak.presentation.model.toExploreFeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreViewModel(
    private val newsRepository: NewsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ExploreSideEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var hasMore = true
    private var loadingJob: Job? = null

    fun updateName(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun updateUrl(value: String) {
        _state.update { it.copy(url = value) }
    }

    fun updateLanguage(value: String) {
        _state.update { it.copy(language = value) }
    }

    fun updatePreference(preference: Preference) {
        _state.update {
            val current = it.selectedPreferences
            val updated = if (preference in current) current - preference else current + preference
            it.copy(selectedPreferences = updated)
        }
    }

    fun reportNewsletter() {
        viewModelScope.launch {
            // TODO: 백엔드 API 연동 전 임시 구현 - 항상 성공으로 처리
            _eventFlow.emit(ExploreSideEffect.ShowReportComplete)
        }
    }

    fun loadFeeds() {
        if (loadingJob != null || !hasMore) return

        loadingJob = viewModelScope.launch {
            val response = newsRepository.getExploreContents()
            val newFeeds = response.contents.map { it.toExploreFeed() }
            hasMore = response.hasMore

            _state.update {
                it.copy(
                    totalCount = response.totalCount,
                    feeds = it.feeds + newFeeds,
                )
            }
        }

        loadingJob?.invokeOnCompletion {
            loadingJob = null
        }
    }
}
