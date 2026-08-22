package com.fairyband.soak.presentation.feature.markdown

/** 마크다운 상세 화면의 상태예요. */
sealed interface MarkdownDetailState {

    data object Loading : MarkdownDetailState

    data class Success(val markdown: String) : MarkdownDetailState

    /** 조회에 실패했거나 본문이 비어 있어 원문 웹뷰로 넘겨야 하는 상태예요. */
    data object Fallback : MarkdownDetailState
}
