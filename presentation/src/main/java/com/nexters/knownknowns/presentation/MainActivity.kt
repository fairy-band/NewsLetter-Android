package com.nexters.knownknowns.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    data object Main : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(Screen.Main)

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (key) {
                        is Screen.Main -> NavEntry(key) {
                            KnownKnownsTheme {
                                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                    Greeting(
                                        name = "Android",
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
                            }
                        }

                        else -> {
                            error("Unknown Route: $key")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier,
            style = KnownKnownsTheme.typography.head28.copy(fontWeight = FontWeight.Bold),
            color = KnownKnownsTheme.colors.statePositivePrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KnownKnownsTheme {
        Greeting("Android")
    }
}
