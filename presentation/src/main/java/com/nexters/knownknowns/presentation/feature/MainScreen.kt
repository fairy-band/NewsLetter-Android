package com.nexters.knownknowns.presentation.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nexters.knownknowns.core.theme.KnownKnownsTheme

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
        )
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
