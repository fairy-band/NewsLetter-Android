package com.fairyband.soak.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalSnackbarController = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState가 주입되지 않았습니다.")
}