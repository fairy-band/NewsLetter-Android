package com.nexters.knownknowns.presentation.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.data.model.NewsResponse
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val news by viewModel.news.collectAsStateWithLifecycle()

    MainScreen(news = news)
}

@Composable
fun MainScreen(
    news: List<NewsResponse>
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn {
            items(news.size) {
                Greeting(
                    name = news[it].title,
                    modifier = Modifier.padding(innerPadding)
                )
            }
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KnownKnownsTheme {
        MainScreen()
    }
}