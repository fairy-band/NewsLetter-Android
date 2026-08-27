package com.fairyband.soak.presentation.feature.markdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.core.extension.suspendRunCatching
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.presentation.navigation.MainDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class MarkdownDetailViewModel(
    private val detail: MainDestination.MarkdownDetail,
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<MarkdownDetailState>(MarkdownDetailState.Loading)
    val state = _state.asStateFlow()

    init {
        loadMarkdown()
    }

    private fun loadMarkdown() {
        viewModelScope.launch {
            suspendRunCatching { newsRepository.getMarkdown(detail.exposureContentId) }
                .onSuccess { markdown ->
                    _state.update {
                        // 본문이 비어 있으면 보여 줄 게 없으니 원문으로 보내요.
                        if (markdown.isBlank()) {
                            MarkdownDetailState.Fallback
                        } else {
                            MarkdownDetailState.Success(markdown)
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.e(throwable)
                    _state.update { MarkdownDetailState.Fallback }
                }
        }
    }
}
