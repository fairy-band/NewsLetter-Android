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
        val totalCount: Int,
    ) : MainDestination("explore_detail")

    /**
     * AI 요약 마크다운 상세 화면입니다.
     *
     * @param title 상단 바에 표시할 뉴스레터명입니다. 본문 첫 헤딩과 겹치지 않도록 제목이 아닌 뉴스레터명을 씁니다.
     * @param pointColorArgb 카드 포인트 컬러입니다. `Color`는 `@Serializable`이 아니라 ARGB `Int`로 전달합니다.
     * @param fallbackUrl 마크다운 조회 실패 시 대신 열 원문 URL입니다.
     */
    @Serializable
    data class MarkdownDetail(
        val exposureContentId: Long,
        val title: String,
        val pointColorArgb: Int,
        val fallbackUrl: String,
    ) : MainDestination("markdown_detail")
}
