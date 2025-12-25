package com.fairyband.soak.presentation.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.explore.ExploreScreen
import com.fairyband.soak.presentation.feature.home.HomeScreen
import com.fairyband.soak.presentation.navigation.NavController
import com.fairyband.soak.presentation.navigation.Screen
import com.fairyband.soak.presentation.navigation.TabDestination
import com.fairyband.soak.presentation.navigation.rememberNavController

@Composable
fun TabScreen() {
    val soakColors = LocalSoakColors.current

    val tabNavController = rememberNavController(TabDestination.Main)
    val isMain by remember {
        derivedStateOf {
            tabNavController.backStack.last() == TabDestination.Main
        }
    }
    val backgroundColor = remember(isMain) {
        if (isMain) soakColors.fillSecondary else soakColors.backgroundSurfaceInverse
    }

    Column(
        modifier = Modifier.background(backgroundColor)
    ) {
        SoakTab(tabNavController = tabNavController, isMain = isMain)

        NavDisplay(
            backStack = tabNavController.backStack,
            onBack = { tabNavController.pop() },
            entryProvider = createEntryProvider(),
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            )
        )
    }
}

@Composable
private fun SoakTab(
    tabNavController: NavController,
    isMain: Boolean,
) {
    val navController = LocalNavController.current
    val density = LocalDensity.current
    val insets = WindowInsets.statusBars
    val statusBarHeight = with(density) { insets.getTop(density).toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp + statusBarHeight),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        tabNavController.navigate(TabDestination.Main)
                    }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(R.string.tab_recommend),
                    style = SoakTheme.typography.body15.copy(
                        color = if (isMain) SoakTheme.colors.textStrong else SoakTheme.colors.textStrongInverse,
                        fontWeight = if (isMain) FontWeight.Bold else FontWeight.Normal
                    ),
                )

                if (isMain) {
                    Box(
                        modifier = Modifier
                            .size(width = 16.dp, height = 2.dp)
                            .background(color = SoakTheme.colors.iconStrong)
                            .clip(shape = CircleShape)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clickable {
                        tabNavController.navigate(TabDestination.Explore)
                    }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(R.string.tab_explore),
                    style = SoakTheme.typography.body15.copy(
                        color = if (!isMain) SoakTheme.colors.textStrongInverse else SoakTheme.colors.textTertiary,
                        fontWeight = if (!isMain) FontWeight.Bold else FontWeight.Normal
                    ),
                )

                if (!isMain) {
                    Box(
                        modifier = Modifier
                            .size(width = 16.dp, height = 2.dp)
                            .background(color = SoakTheme.colors.iconStrongInverse)
                            .clip(shape = CircleShape)
                    )
                }
            }
        }

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navController.navigate(Screen.Setting)
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_setting),
            contentDescription = "설정 화면으로 이동하기",
            tint = if (isMain) SoakTheme.colors.iconPrimary else SoakTheme.colors.iconStrongInverse
        )

    }
}

private fun createEntryProvider(): (NavKey) -> NavEntry<NavKey> {
    return entryProvider {
        entry<TabDestination.Main> { main ->
            HomeScreen()
        }

        entry<TabDestination.Explore> { main ->
            ExploreScreen()
        }
    }
}