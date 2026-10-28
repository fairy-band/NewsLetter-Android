package com.fairyband.soak.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.deeplink.SharedContentDeepLink
import com.fairyband.soak.presentation.deeplink.SharedLinkNavigator
import com.fairyband.soak.presentation.feature.TabScreen
import com.fairyband.soak.presentation.feature.exploredetail.ExploreDetailScreen
import com.fairyband.soak.presentation.feature.markdown.MarkdownDetailScreen
import com.fairyband.soak.presentation.feature.setting.SettingScreen
import com.fairyband.soak.presentation.feature.setting.personal.SettingPersonalScreen
import com.fairyband.soak.presentation.feature.setting.service.SettingServiceScreen
import com.fairyband.soak.presentation.feature.splash.SplashScreen
import com.fairyband.soak.presentation.feature.webview.WebViewScreen
import com.fairyband.soak.presentation.navigation.MainDestination
import com.fairyband.soak.presentation.navigation.NavController
import com.fairyband.soak.presentation.navigation.rememberNavController
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController가 주입되지 않았습니다.")
}

class MainActivity : ComponentActivity() {
    private val sharedLinkNavigator: SharedLinkNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 화면 회전 등으로 다시 생성될 때 같은 링크를 또 처리하지 않도록 처음 생성될 때만 확인해요.
        if (savedInstanceState == null) {
            handleSharedLink(intent)
        }
        setContent {
            SoakTheme {
                val navController = rememberNavController(MainDestination.Splash)

                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = LocalDensity.current.density,
                        fontScale = 1f
                    ),
                    LocalNavController provides navController
                ) {
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleSharedLink(intent)
    }

    /**
     * 카카오톡 공유 링크로 들어오면 보관함 '공유받은' 탭을 열어요.
     * 공유받은 목록 등록은 로그인이 끝난 뒤 탭 화면에서 처리해요.
     */
    private fun handleSharedLink(intent: Intent) {
        val uri = intent.data ?: return
        if (uri.host != SharedContentDeepLink.KAKAO_LINK_HOST) return

        val exposureContentId = SharedContentDeepLink.parse(queryParameter = uri::getQueryParameter)
        if (exposureContentId == null) {
            Timber.w("공유 링크에 콘텐츠 정보가 없어요: $uri")
        }
        sharedLinkNavigator.requestOpenSharedTab(exposureContentId)
    }

    private fun createEntryProvider(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {

            entry<MainDestination.Splash> { main ->
                SplashScreen()
            }

            entry<MainDestination.Home> { main ->
                TabScreen()
            }

            entry<MainDestination.WebView> { webView ->
                WebViewScreen(url = webView.url)
            }

            entry<MainDestination.Setting> {
                SettingScreen()
            }

            entry<MainDestination.SettingService> {
                SettingServiceScreen()
            }

            entry<MainDestination.SettingPersonal> {
                SettingPersonalScreen()
            }

            entry<MainDestination.ExploreDetail> {
                ExploreDetailScreen(koinViewModel(
                    parameters = { parametersOf(it) }
                ))
            }

            entry<MainDestination.MarkdownDetail> { markdownDetail ->
                MarkdownDetailScreen(
                    detail = markdownDetail,
                    viewModel = koinViewModel(
                        parameters = { parametersOf(markdownDetail) }
                    ),
                )
            }
        }
    }
}
