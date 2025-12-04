package com.fairyband.soak.presentation.feature

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fairyband.soak.presentation.feature.home.HomeScreen
import com.fairyband.soak.presentation.feature.search.SearchScreen
import com.fairyband.soak.presentation.navigation.TabDestination
import com.fairyband.soak.presentation.navigation.rememberNavController

@Composable
fun TabScreen() {
    val navController = rememberNavController(TabDestination.Main)

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

private fun createEntryProvider(): (NavKey) -> NavEntry<NavKey> {
    return entryProvider {
        entry<TabDestination.Main> { main ->
            HomeScreen()
        }

        entry<TabDestination.Search> { main ->
            SearchScreen()
        }
    }
}