package com.fairyband.soak.presentation.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.model.request.ContentProviderRequest
import com.fairyband.soak.data.model.request.Direction
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

        val job = viewModelScope.launch {
            val response = newsRepository.getExploreContents(
                direction = state.value.direction,
                categoryIds = state.value.selectedJobFilters.map { it.categoryId },
            )
            val newFeeds = response.contents.map { it.toExploreFeed() }
            hasMore = response.hasMore

            _state.update {
                it.copy(
                    totalCount = response.totalCount,
                    feeds = it.feeds + newFeeds,
                )
            }
        }
        loadingJob = job

        // 취소된 이전 job 의 완료 콜백이 새 job 의 참조를 지우지 않도록 확인해요.
        job.invokeOnCompletion {
            if (loadingJob === job) loadingJob = null
        }
    }

    fun toggleOrder() {
        val newDirection = if (_state.value.direction == Direction.DESC) Direction.ASC else Direction.DESC
        reloadWith { it.copy(direction = newDirection) }
    }

    /**
     * 직군 필터를 토글해요.
     *
     * - 전체(선택 없음) 상태에서 하나를 고르면 해당 직군만 선택돼요.
     * - 모든 직군이 선택되면 자동으로 전체 선택으로 바뀌어요.
     */
    fun toggleJobFilter(preference: Preference) {
        val current = _state.value.selectedJobFilters
        val updated = when {
            current.isEmpty() -> listOf(preference)
            preference in current -> current - preference
            else -> Preference.entries.filter { it in current || it == preference }
        }
        val isAllSelected = updated.size == Preference.entries.size

        reloadWith { it.copy(selectedJobFilters = if (isAllSelected) emptyList() else updated) }
    }

    /**
     * '전체' 칩을 눌렀을 때 모든 직군 필터를 해제해요.
     */
    fun selectAllJobFilters() {
        if (_state.value.selectedJobFilters.isEmpty()) return
        reloadWith { it.copy(selectedJobFilters = emptyList()) }
    }

    /**
     * 정렬/필터 조건이 바뀌면 keyset 커서가 무효해지므로 목록을 비우고 처음부터 다시 조회해요.
     */
    private fun reloadWith(update: (ExploreState) -> ExploreState) {
        loadingJob?.cancel()
        loadingJob = null
        hasMore = true
        _state.update { update(it).copy(feeds = emptyList(), totalCount = 0) }
        loadFeeds()
    }
}