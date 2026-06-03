package com.fairyband.soak.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.fairyband.soak.presentation.analytics.SoakAnalytics
import kotlinx.coroutines.flow.filterNotNull

class NavController(val backStack: NavBackStack) {

    fun replace(dest: NavKey) {
        backStack.add(dest)
        backStack.removeAt(backStack.size - 2) // 원래 화면
    }

    fun navigate(dest: NavKey) {
        backStack.add(dest)
    }

    fun pop() {
        backStack.removeLastOrNull()
    }
}

@Composable
fun rememberNavController(startDestination: NavKey): NavController {
    val backStack = rememberNavBackStack(startDestination)

    LaunchedEffect(backStack) {
        var previousScreenName = "none"

        snapshotFlow { backStack.lastOrNull() }
            .filterNotNull()
            .collect {
                val screenName = (it as? MainDestination)?.name ?: "Unknown"

                SoakAnalytics.logPageView(
                    screenName = screenName,
                    previousScreenName = previousScreenName,
                    arguments = it.toString(),
                )

                previousScreenName = screenName
            }
    }

    return remember { NavController(backStack) }
}