package com.nexters.knownknowns.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * [ColorPalette]
 * 기본적인 컬러를 정의합니다.
 */
private object ColorPalette {
    // Common
    val white: Color = Color(0xFFFFFFFF)
    val black: Color = Color(0xFF000000)

    // Gray
    val gray10: Color = Color(0xFFFAFBFc)
    val gray30: Color = Color(0xFFF7F8FA)
    val gray50: Color = Color(0xFFF2F4F7)
    val gray100: Color = Color(0xFFE8EBF0)
    val gray150: Color = Color(0xFFD7DCE0)
    val gray200: Color = Color(0xFFC9CFD6)
    val gray300: Color = Color(0xFFA3ADB8)
    val gray400: Color = Color(0xFF828E99)
    val gray500: Color = Color(0xFF757F8A)
    val gray600: Color = Color(0xFF5E6870)
    val gray700: Color = Color(0xFF4D545C)
    val gray800: Color = Color(0xFF363B40)
    val gray900: Color = Color(0xFF1B1D1F)
    val gray950: Color = Color(0xFF121212)

    // Red
    val red50: Color = Color(0xFFFDF0F0)
    val red100: Color = Color(0xFFFFE5E5)
    val red150: Color = Color(0xFFFDCACA)
    val red200: Color = Color(0xFFFAA5A5)
    val red300: Color = Color(0xFFF77C7C)
    val red400: Color = Color(0xFFF05454)
    val red500: Color = Color(0xFFF03E3E)
    val red600: Color = Color(0xFFD43636)
    val red700: Color = Color(0xFFBA3030)
    val red800: Color = Color(0xFF902424)
    val red900: Color = Color(0xFF6B1B1B)

    // Green
    val green50: Color = Color(0xFFEBFAEC)
    val green100: Color = Color(0xFFDCFADE)
    val green150: Color = Color(0xFFCAF0CD)
    val green200: Color = Color(0xFF99E59F)
    val green300: Color = Color(0xFF66D86F)
    val green400: Color = Color(0xFF47CB51)
    val green500: Color = Color(0xFF27C434)
    val green600: Color = Color(0xFF05AD13)
    val green700: Color = Color(0xFF098514)
    val green800: Color = Color(0xFF006108)
    val green900: Color = Color(0xFF004005)
}

/**
 * [KnownKnownsColors]
 * 임의로 수정하여 사용할 수 없고, 각 목적에 맞는 컬러를 정의합니다.
 */
data class KnownKnownsColors(
    // Background
    val backgroundBase: Color = ColorPalette.white,
    val backgroundSurface: Color = ColorPalette.white,
    val backgroundElevated: Color = ColorPalette.white,
    val backgroundOnSurface: Color = ColorPalette.gray30,
    val backgroundOnElevated: Color = ColorPalette.gray30,
    val backgroundSurfaceInverse: Color = ColorPalette.gray900,
    val backgroundDimmed: Color = ColorPalette.gray900.copy(alpha = 0.7f),

    // Fill
    val fillPrimary: Color = ColorPalette.gray100,
    val fillSecondary: Color = ColorPalette.gray50,
    val fillTertiary: Color = ColorPalette.gray30,
    val fillDisabled: Color = ColorPalette.gray50,
    val fillPrimaryInverse: Color = ColorPalette.gray950,
    val fillSecondaryInverse: Color = ColorPalette.gray700,
    val fillWhite: Color = ColorPalette.white,

    // Border
    val borderPrimary: Color = ColorPalette.gray100,
    val borderSecondary: Color = ColorPalette.gray50,
    val borderStrong: Color = ColorPalette.gray950,
    val borderDisabled: Color = ColorPalette.gray100,
    val borderActive: Color = ColorPalette.gray300,

    // Divider
    val divider1px: Color = ColorPalette.gray50,
    val divider1pxStrong: Color = ColorPalette.gray100,
    val divider10px: Color = ColorPalette.gray50,

    // Text
    val textStrong: Color = ColorPalette.gray950,
    val textPrimary: Color = ColorPalette.gray900,
    val textSecondary: Color = ColorPalette.gray700,
    val textTertiary: Color = ColorPalette.gray400,
    val textDisabled: Color = ColorPalette.gray300,
    val textStrongInverse: Color = ColorPalette.white,

    // Icon
    val iconStrong: Color = ColorPalette.gray950,
    val iconPrimary: Color = ColorPalette.gray900,
    val iconSecondary: Color = ColorPalette.gray700,
    val iconTertiary: Color = ColorPalette.gray400,
    val iconDisabled: Color = ColorPalette.gray300,
    val iconStrongInverse: Color = ColorPalette.white,

    // State
    val statePositivePrimary: Color = ColorPalette.green500,
    val statePositiveBorder: Color = ColorPalette.green150,
    val statePositiveBackground: Color = ColorPalette.green50,
    val stateNegativePrimary: Color = ColorPalette.red500,
    val stateNegativeBorder: Color = ColorPalette.red150,
    val stateNegativeBackground: Color = ColorPalette.red50,
)
