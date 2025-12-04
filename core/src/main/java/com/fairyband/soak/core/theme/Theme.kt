package com.fairyband.soak.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalSoakColors = staticCompositionLocalOf<SoakColors> {
    error("No SoakColors provided")
}

private val LocalSoakTypography = staticCompositionLocalOf<SoakTypography> {
    error("No LocalSoakTypography provided")
}

object SoakTheme {
    val colors: SoakColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSoakColors.current

    val typography: SoakTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalSoakTypography.current
}

@Composable
fun SoakTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSoakColors provides SoakColors(),
        LocalSoakTypography provides SoakTypography(),
    ) {
        MaterialTheme(
            content = content,
            colorScheme = MaterialTheme.colorScheme.copy(
                background = SoakTheme.colors.fillSecondary
            )
        )
    }
}
