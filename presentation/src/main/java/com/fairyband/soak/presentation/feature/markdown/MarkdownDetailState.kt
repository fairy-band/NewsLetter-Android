package com.fairyband.soak.presentation.feature.markdown

sealed interface MarkdownDetailState {

    data object Loading : MarkdownDetailState

    data class Success(val markdown: String) : MarkdownDetailState

    data object Fallback : MarkdownDetailState
}
