package com.nexters.knownknowns.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
            Cards(

            )
        }
    }
}

@Composable
fun Cards(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier
                .zIndex(5f)
        )
        Card(
            modifier = Modifier
                .zIndex(4f)
                .offset(y = (-80).dp)
                .padding(horizontal = 16.dp)
        )
        Card(
            modifier = Modifier
                .zIndex(3f)
                .offset(y = (-160).dp)
                .padding(horizontal = 32.dp)
        )
        Card(
            modifier = Modifier
                .zIndex(2f)
                .offset(y = (-240).dp)
                .padding(horizontal = 48.dp)
        )
        Card(
            modifier = Modifier
                .zIndex(1f)
                .offset(y = (-320).dp)
                .padding(horizontal = 64.dp)
        )
        Card(
            modifier = Modifier
                .zIndex(0f)
                .offset(y = (-400).dp)
                .padding(horizontal = 80.dp)
        )
    }
}

// 탑, 바텀 마진은 다르다.
// 폰트 사이즈 다르다.
// 위에 세 개는 두 줄일 때 키워드가 없다
// 배경색 다르다.
// 높이가 같다.
// 좌우 마진이 같다.
@Composable
fun Card(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(166.dp)
            .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(color = KnownKnownsTheme.colors.accentGreen)
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 16.dp),
    ) {
        Text(
            text = "어제 먹은 치킨이 맛있었는데 제목이 두 줄이 나오려면 좀 더 길게 작성해야 하는 건에 대하여",
            style = KnownKnownsTheme.typography.body18.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            modifier = Modifier.padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Kotlin",
                style = KnownKnownsTheme.typography.body13.copy(
                    fontWeight = FontWeight.Medium,
                    color = KnownKnownsTheme.colors.textStrong.copy(alpha = 0.5f)
                )
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(width = 1.dp, height = 14.dp)
                    .background(color = Color.Black.copy(alpha = 0.1f))
            )
            Text(
                text = "안드로이드 위클리",
                style = KnownKnownsTheme.typography.body13.copy(
                    fontWeight = FontWeight.Medium,
                    color = KnownKnownsTheme.colors.textStrong.copy(alpha = 0.5f)
                )
            )
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