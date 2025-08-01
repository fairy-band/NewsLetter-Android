package com.nexters.knownknowns.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.feature.detail.DetailScreen
import com.nexters.knownknowns.presentation.feature.home.HomeScreen
import com.nexters.knownknowns.presentation.feature.webview.WebViewScreen
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
                val navController = rememberNavController()

                CompositionLocalProvider(LocalNavController provides navController) {
                    NavDisplay(
                        backStack = navController.backStack,
                        onBack = { navController.pop() },
                        entryProvider = createEntryProvider(),
                        entryDecorators = listOf(
                            rememberSavedStateNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        )
                    )
                }
            }
        }
    }

    private fun createEntryProvider(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<Screen.Home> { main ->
                HomeScreen()
            }

            entry<Screen.Detail> { detail ->
                DetailScreen(title = detail.title)
            }

            entry<Screen.WebView> { webView ->
                WebViewScreen(url = webView.url)
            }
        }
    }
}
