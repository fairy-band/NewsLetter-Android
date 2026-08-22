package com.fairyband.soak.presentation.feature.markdown

import androidx.compose.ui.graphics.Color

/**
 * 마크다운 상세 화면 전용 컬러예요.
 *
 * 이 화면은 iOS와 동일하게 **다크 배경 고정**이라 앱 전역 라이트 테마(`SoakColors`)와
 * 맞지 않아요. `SoakColors`에는 다크 토큰이 없으므로 화면 안에서만 쓰는 값을 여기에 모아 둬요.
 * 앱 전역 테마는 건드리지 않아요.
 *
 * iOS는 라이트 톤(`gray30`, `pointGreen50` 등)을 그대로 쓰지만 화면 배경은 `gray900`이라
 * 밝은 카드가 떠 보여요. 안드로이드는 같은 구조(배경/보더/강조)를 유지하되 다크 배경에 맞게
 * 톤을 뒤집었어요. 디자인 확정 전까지의 잠정값이에요.
 */
internal object MarkdownColors {

    // 화면 / 텍스트
    val background: Color = Color(0xFF1B1D1F) // gray900
    val text: Color = Color(0xFFC9CFD6) // gray200
    val textStrong: Color = Color(0xFFFFFFFF) // white
    val textTertiary: Color = Color(0xFFA3ADB8) // gray300
    val divider: Color = Color(0xFF4D545C) // gray700

    // 코드
    val codeBackground: Color = Color(0xFF262A2E)
    val codeBorder: Color = Color(0xFF4D545C) // gray700
    val codeText: Color = Color(0xFFE8EBF0) // gray100
    val codeLanguageBackground: Color = Color(0xFF363B40) // gray800
    val inlineCodeBackground: Color = Color(0xFF363B40) // gray800

    // 인용
    val quoteBar: Color = Color(0xFF7FBBFF) // blue400
    val quoteBackground: Color = quoteBar.copy(alpha = 0.10f)
    val quoteBorder: Color = quoteBar.copy(alpha = 0.25f)

    // 표
    val tableHeaderBackground: Color = Color(0xFF363B40) // gray800
    val tableStripeBackground: Color = Color(0x0DFFFFFF)

    /** Callout 타입별 강조 색이에요. 배경/보더는 이 색의 알파를 낮춰 파생해요. */
    fun accentOf(type: CalloutType): Color = when (type) {
        CalloutType.TIP -> Color(0xFF41D17F) // greenTextPrimary
        CalloutType.INFO -> Color(0xFF68A4E7) // blueTextPrimary
        CalloutType.WARNING -> Color(0xFFF38338) // orangeTextPrimary
        CalloutType.DANGER -> Color(0xFFF77C7C) // red300
        CalloutType.NOTE -> Color(0xFFA3ADB8) // gray300
        CalloutType.IMPORTANT -> Color(0xFFA888C7) // purpleTextPrimary
    }
}
