package com.fairyband.soak.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * @param name firebase analytics에 기록할 화면 이름을 의미합니다.
 */
@Serializable
sealed class Screen(val name: String) : NavKey {

    @Serializable
    data object Splash : Screen("splash")

    @Serializable
    data object Home : Screen("main")

    @Serializable
    data class WebView(
        val url: String,
    ) : Screen("webview")

    // 실제 화면은 아니지만, GA 로그를 찍기 위해 관리하는 객체들. 그러나 나중에 백스택으로 관리 가능할지 검토할 수 있음.
    @Serializable
    data object NewsLetterCarousel : Screen("newsletter_carousel")

    @Serializable
    data object BottomSheetNotification : Screen("bottom_sheet_notification")

    @Serializable
    data object BottomSheetCustom : Screen("bottom_sheet_custom")
}
