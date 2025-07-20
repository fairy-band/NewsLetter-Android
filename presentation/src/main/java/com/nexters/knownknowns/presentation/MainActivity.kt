package com.nexters.knownknowns.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.feature.main.MainScreen
import com.nexters.knownknowns.presentation.navigation.Screen
import com.nexters.knownknowns.presentation.navigation.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KnownKnownsTheme {
                val navController = rememberNavController()

                NavDisplay(
                    backStack = navController.backStack,
                    onBack = { navController.pop() },
                    entryProvider = createEntryProvider()
                )
            }
        }
    }

    fun createEntryProvider(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<Screen.Main> { entry ->
                MainScreen()
            }
        }
    }
}
