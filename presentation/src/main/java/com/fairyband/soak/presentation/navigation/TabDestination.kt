package com.fairyband.soak.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * @param name firebase analytics에 기록할 화면 이름을 의미합니다.
 */
@Serializable
sealed class TabDestination(val name: String) : NavKey {
    @Serializable
    data object Main : Screen("main")

    @Serializable
    data object Search : Screen("search")
}
