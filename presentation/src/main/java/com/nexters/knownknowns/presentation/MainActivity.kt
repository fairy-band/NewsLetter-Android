package com.nexters.knownknowns.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.feature.detail.DetailScreen
import com.nexters.knownknowns.presentation.feature.main.MainScreen
import com.nexters.knownknowns.presentation.navigation.NavController
import com.nexters.knownknowns.presentation.navigation.Screen
import com.nexters.knownknowns.presentation.navigation.rememberNavController

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController가 주입되지 않았습니다.")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KnownKnownsTheme {
                val backStack = rememberNavBackStack(Screen.Main)
                val navController = rememberNavController()
//
//                LaunchedEffect(navController.backStack) {
//                    println("navigated ${navController.backStack}")
//                }

                CompositionLocalProvider(LocalNavController provides navController) {
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = createEntryProvider(backStack)
                    )
                }
            }
        }
    }

    fun createEntryProvider(backStack: NavBackStack): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<Screen.Main> { main ->
                MainScreen(backStack)
            }

            entry<Screen.Detail> { detail ->
                DetailScreen(detail.title)
            }
        }
    }
}
