package com.fairyband.soak.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * @param name firebase analytics에 기록할 화면 이름을 의미합니다.
 */
@Serializable
sealed class Screen(val name: String) : NavKey {

    @Serializable
    data object Home : Screen("main")

    @Serializable
    data class WebView(
        val url: String,
    ) : Screen("webview")

    @Serializable
    data object Setting : Screen("setting")

    @Serializable
    data object SettingService : Screen("setting_service")

    @Serializable
    data object SettingPersonal: Screen("setting_personal")

    // 실제 화면은 아니지만, GA 로그를 찍기 위해 관리하는 객체들. 그러나 나중에 백스택으로 관리 가능할지 검토할 수 있음.
    data object NewsLetterCarousel : Screen("newsletter_carousel")
    data object BottomSheetNotification : Screen("bottom_sheet_notification")
    data object BottomSheetCustom : Screen("bottom_sheet_custom")
}
