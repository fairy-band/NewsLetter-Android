package com.fairyband.soak.presentation.feature.explore

sealed class ExploreSideEffect {
    data object ShowReportComplete : ExploreSideEffect()
}
