package com.fairyband.soak.presentation.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.request.Sort
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

    fun resetReportState() {
        _state.update { it.copy(name = "", url = "", selectedPreferences = emptyList(), language = "") }
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
            val state = _state.value
            val request = ContentProviderRequest(
                name = state.name,
                url = state.url,
                positions = state.selectedPreferences.map { it.stringValue },
                language = state.language,
            )
            // TODO: 에러 케이스 디자인이 추가되면 처리해 주세요.
            newsRepository.requestContentProvider(request)
            _eventFlow.emit(ExploreSideEffect.ShowReportComplete)
        }
    }

    fun loadFeeds() {
        if (loadingJob != null || !hasMore) return

        loadingJob = viewModelScope.launch {
            val response = newsRepository.getExploreContents(state.value.sort)
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

    fun toggleOrder() {
        val newSort = if (_state.value.sort == Sort.PUBLISHED) Sort.REGISTERED else Sort.PUBLISHED
        loadingJob?.cancel()
        loadingJob = null
        hasMore = true
        _state.update { it.copy(sort = newSort, feeds = emptyList(), totalCount = 0) }
        loadFeeds()
    }
}