package com.nexters.knownknowns.presentation.feature.home

sealed class HomeSideEffect {
    data object ShowBottomSheet : HomeSideEffect()
}
