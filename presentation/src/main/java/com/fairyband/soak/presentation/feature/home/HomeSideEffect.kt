package com.fairyband.soak.presentation.feature.home

sealed class HomeSideEffect {
    data object ShowBottomSheet : HomeSideEffect()
    data class ShowBookmarkResult(val isBookmarked: Boolean) : HomeSideEffect()
}
