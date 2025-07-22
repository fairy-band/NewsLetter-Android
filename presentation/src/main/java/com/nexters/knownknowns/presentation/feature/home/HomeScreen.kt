package com.nexters.knownknowns.presentation.feature.home

import androidx.compose.foundation.clickable
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
import com.nexters.knownknowns.presentation.LocalNavController
import com.nexters.knownknowns.presentation.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val news by viewModel.news.collectAsStateWithLifecycle()

    HomeScreen(news = news)
}

@Composable
fun HomeScreen(
    news: List<NewsResponse>
) {
    val navController = LocalNavController.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn {
            items(news.size) {
                Greeting(
                    name = news[it].title,
                    modifier = Modifier
                        .padding(innerPadding)
                        .clickable {
                            navController.navigate(Screen.Detail(news[it].title))
                        }
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
//        MainScreen()
    }
}