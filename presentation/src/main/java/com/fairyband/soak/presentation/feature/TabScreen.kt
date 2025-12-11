package com.fairyband.soak.presentation.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.SoakColors
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.HomeScreen
import com.fairyband.soak.presentation.feature.search.ExploreScreen
import com.fairyband.soak.presentation.navigation.Screen
import com.fairyband.soak.presentation.navigation.TabDestination
import com.fairyband.soak.presentation.navigation.rememberNavController

@Composable
fun TabScreen() {
    val soakColors = remember { SoakColors() }
    val navController = rememberNavController(TabDestination.Main)
    val backgroundColor by remember(navController.backStack) {
        derivedStateOf {
            if (navController.backStack == TabDestination.Main) {
                soakColors.fillSecondary
            } else {
                soakColors.backgroundSurfaceInverse
            }
        }
    }

    Column(
        modifier = Modifier.background(soakColors.fillSecondary)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(TabDestination.Main)
                    },
                    text = "추천"
                )
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(TabDestination.Explore)
                    },
                    text = "탐색"
                )
            }

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_setting),
                contentDescription = "setting button",
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        end = 8.dp
                    )
                    .align(alignment = Alignment.CenterEnd)
                    .noRippleClickable {
                        navController.navigate(Screen.Setting)
                    }
            )
        }


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