package com.nexters.knownknowns.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.data.model.NewsResponse
import com.nexters.knownknowns.presentation.LocalNavController
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
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO: 오늘 날짜로 변경하기
            Text(
                modifier = Modifier
                    .padding(top = 44.dp)
                    .padding(horizontal = 20.dp), // TODO: 좌우 마진 상수화 하기
                text = "2025.07.08\nToday's Hot News", // TODO: 리소스화
                style = KnownKnownsTheme.typography.title,
                color = KnownKnownsTheme.colors.textStrong,
            )
            // TODO: timer 적용하기
            Text("00:43:43")
            Spacer(modifier = Modifier.weight(1f))
            Cards()
        }
    }
}

@Composable
fun Cards() {
    Column {
        Card()
        Card()
        Card()
        Card()
        Card()
        Card()
    }
}

@Composable
fun Card() {
    Column {
        Text("어제 먹은 치킨이 맛있었는데 제목이 두 줄이 나오려면 좀 더 길게 작성해야 하는 건에 대하여")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kotlin")
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 14.dp)
                    .background(color = Color.Black.copy(alpha = 0.1f))
            )
            Text("안드로이드 위클리")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KnownKnownsTheme {
//        MainScreen()
    }
}