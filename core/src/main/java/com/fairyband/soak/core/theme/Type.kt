package com.fairyband.soak.core.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.fairyband.soak.core.R

val pretendardFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_bold, FontWeight.Bold)
)

val jalnanGothicFamily = FontFamily(
    Font(R.font.jalnan_gothic, FontWeight.Normal),
)

data class KnownKnownsTypography(
    val title: TextStyle = createTextStyle(
        fontFamily = jalnanGothicFamily,
        fontSize = 32.sp,
        lineHeight = 1.4.em,
        fontWeight = FontWeight.Normal,
        letterSpacing = (-1).sp
    ),
    val head28: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 28.sp,
        lineHeight = 1.36.em
    ),
    val head26: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 26.sp,
        lineHeight = 1.35.em
    ),
    val head24: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 24.sp,
        lineHeight = 1.34.em
    ),
    val head22: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 22.sp,
        lineHeight = 1.4.em
    ),
    val head20: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 20.sp,
        lineHeight = 1.4.em
    ),
    val body18: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 18.sp,
        lineHeight = 1.45.em
    ),
    val body16: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 16.sp,
        lineHeight = 1.5.em
    ),
    val body15: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 15.sp,
        lineHeight = 1.47.em
    ),
    val body14: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 14.sp,
        lineHeight = 1.43.em
    ),
    val body13: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 13.sp,
        lineHeight = 1.38.em
    ),
    val caption12: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 12.sp,
        lineHeight = 1.35.em
    ),
    val caption11: TextStyle = createTextStyle(
        fontFamily = pretendardFamily,
        fontSize = 11.sp,
        lineHeight = 1.28.em
    ),
)

private fun createTextStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    fontFamily: FontFamily = pretendardFamily,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = TextUnit.Unspecified,
): TextStyle {
    return TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        letterSpacing = letterSpacing,
    )
}
