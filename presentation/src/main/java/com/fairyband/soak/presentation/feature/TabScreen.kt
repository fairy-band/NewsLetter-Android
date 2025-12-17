package com.fairyband.soak.presentation.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
    val navController = rememberNavController(TabDestination.Main)
    val isMain by remember {
        derivedStateOf {
            navController.backStack.last() == TabDestination.Main
        }
    }
    val backgroundColor by remember(navController.backStack) {
        derivedStateOf {
            if (isMain) soakColors.fillSecondary else soakColors.backgroundSurfaceInverse
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(backgroundColor)
    ) {
        SoakTab(navController = navController, isMain = isMain)

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

@Composable
private fun SoakTab(
    navController: NavController,
    isMain: Boolean,
) {
    val soakColors = LocalSoakColors.current
    val textColor by remember(navController.backStack) {
        derivedStateOf {
            if (isMain) soakColors.textStrong else soakColors.textStrongInverse
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(soakColors.purpleBackgroundPrimary),
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
                        navController.navigate(TabDestination.Main)
                    }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.tab_recommend),
                    style = SoakTheme.typography.body15.copy(color = textColor),
                )
            }
            Box(
                modifier = Modifier
                    .clickable {
                        navController.navigate(TabDestination.Explore)
                    }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.tab_explore),
                    style = SoakTheme.typography.body15.copy(color = textColor),
                )
            }
        }

        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navController.navigate(Screen.Setting)
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_setting),
            contentDescription = "설정 화면으로 이동하기",
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