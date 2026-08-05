package com.fairyband.soak.presentation.feature.setting

sealed class SettingSideEffect {
    data object UserInfoChanged : SettingSideEffect()
}
