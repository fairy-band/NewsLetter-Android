package com.fairyband.soak.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.fairyband.soak.presentation.model.ExploreFeed
import kotlinx.serialization.Serializable

/**
 * @param name firebase analytics에 기록할 화면 이름을 의미합니다.
 */
@Serializable
sealed class MainDestination(val name: String) : NavKey {

    @Serializable
    data object Splash : MainDestination("splash")

    @Serializable
    data object Home : MainDestination("tab")

    @Serializable
    data class WebView(
        val url: String,
    ) : MainDestination("webview")

    @Serializable
    data object Setting : MainDestination("setting")

    @Serializable
    data object SettingService : MainDestination("setting_service")

    @Serializable
    data object SettingPersonal: MainDestination("setting_personal")

    // 실제 화면은 아니지만, GA 로그를 찍기 위해 관리하는 객체들. 그러나 나중에 백스택으로 관리 가능할지 검토할 수 있음.
    @Serializable
    data object NewsLetterCarousel : MainDestination("newsletter_carousel")

    @Serializable
    data object BottomSheetNotification : MainDestination("bottom_sheet_notification")

    @Serializable
    data object BottomSheetCustom : MainDestination("bottom_sheet_custom")

    @Serializable
    data class ExploreDetail(
        val index: Int,
        val feeds: List<ExploreFeed>,
    ) : MainDestination("explore_detail")
}
