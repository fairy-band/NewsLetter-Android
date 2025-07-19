package com.nexters.knownknowns.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalKnownKnownsColors = staticCompositionLocalOf<KnownKnownsColors> {
    error("No KnownKnownsColors provided")
}

private val LocalKnownKnownsTypography = staticCompositionLocalOf<KnownKnownsTypography> {
    error("No LocalKnownKnownsTypography provided")
}

object KnownKnownsTheme {
    val colors: KnownKnownsColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKnownKnownsColors.current

    val typography: KnownKnownsTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKnownKnownsTypography.current
}

@Composable
fun KnownKnownsTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalKnownKnownsColors provides KnownKnownsColors(),
        LocalKnownKnownsTypography provides KnownKnownsTypography(),
    ) {
        MaterialTheme(content = content)
    }
}
