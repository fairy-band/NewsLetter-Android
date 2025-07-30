package com.nexters.knownknowns.presentation.navigation

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
    data class Detail(
        val title: String,
    ) : Screen("detail")

    @Serializable
    data class WebView(
        val url: String,
    ) : Screen("webview")
}
