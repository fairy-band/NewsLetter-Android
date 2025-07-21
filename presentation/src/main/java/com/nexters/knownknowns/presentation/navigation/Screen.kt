package com.nexters.knownknowns.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {

    @Serializable
    data object Main : Screen

    @Serializable
    data class Detail(
        val title: String,
    ) : Screen
}
