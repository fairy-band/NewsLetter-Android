package com.nexters.knownknowns.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalKnownKnownsColors = staticCompositionLocalOf<KnownKnownsColors> {
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
fun ProvideKnownKnownsColorsAndTypography(
    colors: KnownKnownsColors,
    typography: KnownKnownsTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }.apply { update(typography) }
    CompositionLocalProvider(
        LocalKnownKnownsColors provides provideColors,
        LocalKnownKnownsTypography provides provideTypography,
        content = content
    )
}

@Composable
fun KnownKnownsTheme(
    content: @Composable () -> Unit
) {
    val colors = KnownKnownsColors()
    val typography = KnownKnownsTypography()

    ProvideKnownKnownsColorsAndTypography(
        colors,
        typography
    ) {
        MaterialTheme(content = content)
    }
}
