package com.fairyband.soak.presentation

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
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.feature.home.HomeScreen
import com.fairyband.soak.presentation.feature.setting.SettingScreen
import com.fairyband.soak.presentation.feature.setting.SettingServiceScreen
import com.fairyband.soak.presentation.feature.webview.WebViewScreen
import com.fairyband.soak.presentation.navigation.NavController
import com.fairyband.soak.presentation.navigation.Screen
import com.fairyband.soak.presentation.navigation.rememberNavController

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController가 주입되지 않았습니다.")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoakTheme {
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

            entry<Screen.WebView> { webView ->
                WebViewScreen(url = webView.url)
            }

            entry<Screen.Setting> {
                SettingScreen()
            }

            entry<Screen.SettingService> {
                SettingServiceScreen()
            }
        }
    }
}
