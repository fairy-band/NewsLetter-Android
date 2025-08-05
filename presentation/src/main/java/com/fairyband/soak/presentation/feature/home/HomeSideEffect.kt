package com.fairyband.soak.presentation.feature.home

sealed class HomeSideEffect {
    data object ShowBottomSheet : HomeSideEffect()
}
