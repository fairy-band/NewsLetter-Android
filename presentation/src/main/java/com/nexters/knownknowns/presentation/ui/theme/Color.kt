package com.nexters.knownknowns.presentation.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Background
val BackgroundBase = Color(0xFFF7F8FA)
val BackgroundSurface = Color(0xFFFFFFFF)
val BackgroundElevated = Color(0xFFFFFFFF)
val BackgroundOnSurface = Color(0xFFF7F8FA)
val BackgroundOnElevated = Color(0xFFF7F8FA)
val BackgroundSurfaceInverse = Color(0xFF1B1D1F)
val BackgroundDimmed = Color(0xB3000000)

// Fill
val FillPrimary = Color(0xFFE8EBF0)
val FillSecondary = Color(0xFFF2F4F7)
val FillTertiary = Color(0xFFF7F8FA)
val FillDisabled = Color(0xFFF2F4F7)
val FillPrimaryInverse = Color(0xFF121212)
val FillSecondaryInverse = Color(0xFF4D545C)
val FillWhite = Color(0xFFFFFFFF)

// Border
val BorderPrimary = Color(0xFFE8EBF0)
val BorderSecondary = Color(0xFFD7DCE0)
val BorderStrong = Color(0xFF121212)
val BorderDisabled = Color(0xFFE8EBF0)
val BorderActive = Color(0xFFA3ADB8)

// Divider
val Divider1px = Color(0xFFF2F4F7)
val Divider1pxStrong = Color(0xFFE8EBF0)
val Divider10px = Color(0xFFF2F4F7)

// Text
val TextStrong = Color(0xFF121212)
val TextPrimary = Color(0xFF1B1D1F)
val TextSecondary = Color(0xFF4D545C)
val TextTertiary = Color(0xFF828E99)
val TextDisabled = Color(0xFFA3ADB8)
val TextStrongInverse = Color(0xFFFFFFFF)

// Icon
val IconStrong = Color(0xFF121212)
val IconPrimary = Color(0xFF1B1D1F)
val IconSecondary = Color(0xFF4D545C)
val IconTertiary = Color(0xFF828E99)
val IconDisabled = Color(0xFFA3ADB8)
val IconStrongInverse = Color(0xFFFFFFFF)

// State
val StatePositivePrimary = Color(0xFF27C434)
val StatePositiveBorder = Color(0xFFCAF0CD)
val StatePositiveBackground = Color(0xFFEBFAEC)
val StateNegativePrimary = Color(0xFFF03E3E)
val StateNegativeBorder = Color(0xFFFDCACA)
val StateNegativeBackground = Color(0xFFFDF0F0)

@Stable
class KnownKnownsColors(
    backgroundBase: Color,
    backgroundSurface: Color,
    backgroundElevated: Color,
    backgroundOnSurface: Color,
    backgroundOnElevated: Color,
    backgroundSurfaceInverse: Color,
    backgroundDimmed: Color,
    fillPrimary: Color,
    fillSecondary: Color,
    fillTertiary: Color,
    fillDisabled: Color,
    fillPrimaryInverse: Color,
    fillSecondaryInverse: Color,
    fillWhite: Color,
    borderPrimary: Color,
    borderSecondary: Color,
    borderStrong: Color,
    borderDisabled: Color,
    borderActive: Color,
    divider1px: Color,
    divider1pxStrong: Color,
    divider10px: Color,
    textStrong: Color,
    textPrimary: Color,
    textSecondary: Color,
    textTertiary: Color,
    textDisabled: Color,
    textStrongInverse: Color,
    iconStrong: Color,
    iconPrimary: Color,
    iconSecondary: Color,
    iconTertiary: Color,
    iconDisabled: Color,
    iconStrongInverse: Color,
    statePositivePrimary: Color,
    statePositiveBorder: Color,
    statePositiveBackground: Color,
    stateNegativePrimary: Color,
    stateNegativeBorder: Color,
    stateNegativeBackground: Color,
) {
    var backgroundBase by mutableStateOf(backgroundBase)
        private set
    var backgroundSurface by mutableStateOf(backgroundSurface)
        private set
    var backgroundElevated by mutableStateOf(backgroundElevated)
        private set
    var backgroundOnSurface by mutableStateOf(backgroundOnSurface)
        private set
    var backgroundOnElevated by mutableStateOf(backgroundOnElevated)
        private set
    var backgroundSurfaceInverse by mutableStateOf(backgroundSurfaceInverse)
        private set
    var backgroundDimmed by mutableStateOf(backgroundDimmed)
        private set
    var fillPrimary by mutableStateOf(fillPrimary)
        private set
    var fillSecondary by mutableStateOf(fillSecondary)
        private set
    var fillTertiary by mutableStateOf(fillTertiary)
        private set
    var fillDisabled by mutableStateOf(fillDisabled)
        private set
    var fillPrimaryInverse by mutableStateOf(fillPrimaryInverse)
        private set
    var fillSecondaryInverse by mutableStateOf(fillSecondaryInverse)
        private set
    var fillWhite by mutableStateOf(fillWhite)
        private set
    var borderPrimary by mutableStateOf(borderPrimary)
        private set
    var borderSecondary by mutableStateOf(borderSecondary)
        private set
    var borderStrong by mutableStateOf(borderStrong)
        private set
    var borderDisabled by mutableStateOf(borderDisabled)
        private set
    var borderActive by mutableStateOf(borderActive)
        private set
    var divider1px by mutableStateOf(divider1px)
        private set
    var divider1pxStrong by mutableStateOf(divider1pxStrong)
        private set
    var divider10px by mutableStateOf(divider10px)
        private set
    var textStrong by mutableStateOf(textStrong)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textTertiary by mutableStateOf(textTertiary)
        private set
    var textDisabled by mutableStateOf(textDisabled)
        private set
    var textStrongInverse by mutableStateOf(textStrongInverse)
        private set
    var iconStrong by mutableStateOf(iconStrong)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var iconSecondary by mutableStateOf(iconSecondary)
        private set
    var iconTertiary by mutableStateOf(iconTertiary)
        private set
    var iconDisabled by mutableStateOf(iconDisabled)
        private set
    var iconStrongInverse by mutableStateOf(iconStrongInverse)
        private set
    var statePositivePrimary by mutableStateOf(statePositivePrimary)
        private set
    var statePositiveBorder by mutableStateOf(statePositiveBorder)
        private set
    var statePositiveBackground by mutableStateOf(statePositiveBackground)
        private set
    var stateNegativePrimary by mutableStateOf(stateNegativePrimary)
        private set
    var stateNegativeBorder by mutableStateOf(stateNegativeBorder)
        private set
    var stateNegativeBackground by mutableStateOf(stateNegativeBackground)
        private set

    fun copy(): KnownKnownsColors = KnownKnownsColors(
        backgroundBase,
        backgroundSurface,
        backgroundElevated,
        backgroundOnSurface,
        backgroundOnElevated,
        backgroundSurfaceInverse,
        backgroundDimmed,
        fillPrimary,
        fillSecondary,
        fillTertiary,
        fillDisabled,
        fillPrimaryInverse,
        fillSecondaryInverse,
        fillWhite,
        borderPrimary,
        borderSecondary,
        borderStrong,
        borderDisabled,
        borderActive,
        divider1px,
        divider1pxStrong,
        divider10px,
        textStrong,
        textPrimary,
        textSecondary,
        textTertiary,
        textDisabled,
        textStrongInverse,
        iconStrong,
        iconPrimary,
        iconSecondary,
        iconTertiary,
        iconDisabled,
        iconStrongInverse,
        statePositivePrimary,
        statePositiveBorder,
        statePositiveBackground,
        stateNegativePrimary,
        stateNegativeBorder,
        stateNegativeBackground,
    )

    fun update(other: KnownKnownsColors) {
        backgroundBase = other.backgroundBase
        backgroundSurface = other.backgroundSurface
        backgroundElevated = other.backgroundElevated
        backgroundOnSurface = other.backgroundOnSurface
        backgroundOnElevated = other.backgroundOnElevated
        backgroundSurfaceInverse = other.backgroundSurfaceInverse
        backgroundDimmed = other.backgroundDimmed
        fillPrimary = other.fillPrimary
        fillSecondary = other.fillSecondary
        fillTertiary = other.fillTertiary
        fillDisabled = other.fillDisabled
        fillPrimaryInverse = other.fillPrimaryInverse
        fillSecondaryInverse = other.fillSecondaryInverse
        fillWhite = other.fillWhite
        borderPrimary = other.borderPrimary
        borderSecondary = other.borderSecondary
        borderStrong = other.borderStrong
        borderDisabled = other.borderDisabled
        borderActive = other.borderActive
        divider1px = other.divider1px
        divider1pxStrong = other.divider1pxStrong
        divider10px = other.divider10px
        textStrong = other.textStrong
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textTertiary = other.textTertiary
        textDisabled = other.textDisabled
        textStrongInverse = other.textStrongInverse
        iconStrong = other.iconStrong
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        iconTertiary = other.iconTertiary
        iconDisabled = other.iconDisabled
        iconStrongInverse = other.iconStrongInverse
        statePositivePrimary = other.statePositivePrimary
        statePositiveBorder = other.statePositiveBorder
        statePositiveBackground = other.statePositiveBackground
        stateNegativePrimary = other.stateNegativePrimary
        stateNegativeBorder = other.stateNegativeBorder
        stateNegativeBackground = other.stateNegativeBackground
    }
}

fun KnownKnownsColors(): KnownKnownsColors = KnownKnownsColors(
    backgroundBase = BackgroundBase,
    backgroundSurface = BackgroundSurface,
    backgroundElevated = BackgroundElevated,
    backgroundOnSurface = BackgroundOnSurface,
    backgroundOnElevated = BackgroundOnElevated,
    backgroundSurfaceInverse = BackgroundSurfaceInverse,
    backgroundDimmed = BackgroundDimmed,
    fillPrimary = FillPrimary,
    fillSecondary = FillSecondary,
    fillTertiary = FillTertiary,
    fillDisabled = FillDisabled,
    fillPrimaryInverse = FillPrimaryInverse,
    fillSecondaryInverse = FillSecondaryInverse,
    fillWhite = FillWhite,
    borderPrimary = BorderPrimary,
    borderSecondary = BorderSecondary,
    borderStrong = BorderStrong,
    borderDisabled = BorderDisabled,
    borderActive = BorderActive,
    divider1px = Divider1px,
    divider1pxStrong = Divider1pxStrong,
    divider10px = Divider10px,
    textStrong = TextStrong,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    textTertiary = TextTertiary,
    textDisabled = TextDisabled,
    textStrongInverse = TextStrongInverse,
    iconStrong = IconStrong,
    iconPrimary = IconPrimary,
    iconSecondary = IconSecondary,
    iconTertiary = IconTertiary,
    iconDisabled = IconDisabled,
    iconStrongInverse = IconStrongInverse,
    statePositivePrimary = StatePositivePrimary,
    statePositiveBorder = StatePositiveBorder,
    statePositiveBackground = StatePositiveBackground,
    stateNegativePrimary = StateNegativePrimary,
    stateNegativeBorder = StateNegativeBorder,
    stateNegativeBackground = StateNegativeBackground,
)
