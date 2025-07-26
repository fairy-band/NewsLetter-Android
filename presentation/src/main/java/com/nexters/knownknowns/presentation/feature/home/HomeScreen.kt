package com.nexters.knownknowns.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.LocalNavController
import com.nexters.knownknowns.presentation.R
import com.nexters.knownknowns.presentation.model.NewsFeed
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.koin.compose.viewmodel.koinViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val news by viewModel.news.collectAsStateWithLifecycle()

    HomeScreen(news = news)
}

@Composable
private fun HomeScreen(
    news: ImmutableList<NewsFeed>,
) {
    // TODO: 이거 추가해서 내비게이션 하면 되는데, CompositionLocal이 프리뷰에 문제가 있어서 필요한 사람이 해결하겠지 ㅎㅎ
//    val navController = LocalNavController.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val today = LocalDate.now()
            Text(
                modifier = Modifier
                    .padding(top = 44.dp)
                    .padding(horizontal = 20.dp),
                text = stringResource(
                    R.string.home_title,
                    today.year,
                    today.monthValue.toString().padStart(2, '0'),
                    today.dayOfMonth.toString().padStart(2, '0')
                ),
                style = KnownKnownsTheme.typography.title.copy(textAlign = TextAlign.Center),
                color = KnownKnownsTheme.colors.textStrong,
            )
            Timer()
            Spacer(modifier = Modifier.weight(1f))
            Cards(
                news = news
            )
        }
    }
}

@Composable
private fun Timer() {
    val remainingUntilTomorrow by flow {
        while (true) {
            val now = LocalDateTime.now()
            val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
            val duration = Duration.between(now, midnight)

            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            val seconds = duration.seconds % 60
            emit(Triple(first = hours, second = minutes, third = seconds % 60))
            delay(200)
        }
    }.collectAsStateWithLifecycle(Triple(0L, 0L, 0L))

    Row(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.Top,
    ) {
        val numberStyle = KnownKnownsTheme.typography.body16.copy(
            fontWeight = FontWeight.SemiBold,
            color = KnownKnownsTheme.colors.stateNegativePrimary
        )
        val colonStyle = KnownKnownsTheme.typography.body15.copy(
            fontWeight = FontWeight.Bold,
            color = KnownKnownsTheme.colors.stateNegativePrimary
        )

        val hh = remainingUntilTomorrow.first.toString().padStart(2, '0')
        val mm = remainingUntilTomorrow.second.toString().padStart(2, '0')
        val ss = remainingUntilTomorrow.third.toString().padStart(2, '0')

        Text(hh, style = numberStyle)
        Text(":", style = colonStyle)
        Text(mm, style = numberStyle)
        Text(":", style = colonStyle)
        Text(ss, style = numberStyle)
    }
}

@Composable
private fun Cards(
    news: ImmutableList<NewsFeed>,
    modifier: Modifier = Modifier
) {
    var card1Height by remember { mutableIntStateOf(0) }
    var card2Height by remember { mutableIntStateOf(0) }
    var card3Height by remember { mutableIntStateOf(0) }
    var card4Height by remember { mutableIntStateOf(0) }
    var card5Height by remember { mutableIntStateOf(0) }
    var card6Height by remember { mutableIntStateOf(0) }
    val card2Offset by remember { derivedStateOf { card1Height + card2Height } }
    val card3Offset by remember { derivedStateOf { card2Offset + card3Height } }
    val card4Offset by remember { derivedStateOf { card3Offset + card4Height } }
    val card5Offset by remember { derivedStateOf { card4Offset + card5Height } }
    val card6Offset by remember { derivedStateOf { card5Offset + card6Height } }

    if (news.size < 6) {
        // TODO: loading or error
        return
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .zIndex(0f)
                .offset(y = (166 - card6Offset).dp)
                .padding(horizontal = 80.dp),
            feed = news[5],
            cardColor = KnownKnownsTheme.colors.accentPurple,
            topPadding = 16.dp,
            bottomPadding = 12.dp,
            textStyle = KnownKnownsTheme.typography.body13.copy(
                fontWeight = FontWeight.Bold,
                color = KnownKnownsTheme.colors.textStrong
            ),
            onHeightInflated = { height -> card6Height = height }
        )
        Card(
            modifier = Modifier
                .zIndex(1f)
                .offset(y = (166 - card5Offset).dp)
                .padding(horizontal = 64.dp),
            feed = news[4],
            cardColor = KnownKnownsTheme.colors.accentOrange,
            topPadding = 16.dp,
            bottomPadding = 12.dp,
            textStyle = KnownKnownsTheme.typography.body14.copy(
                fontWeight = FontWeight.Bold,
                color = KnownKnownsTheme.colors.textStrong
            ),
            onHeightInflated = { height -> card5Height = height }
        )
        Card(
            modifier = Modifier
                .zIndex(2f)
                .offset(y = (166 - card4Offset).dp)
                .padding(horizontal = 48.dp),
            feed = news[3],
            cardColor = KnownKnownsTheme.colors.accentSkyBlue,
            topPadding = 20.dp,
            bottomPadding = 16.dp,
            textStyle = KnownKnownsTheme.typography.body15.copy(
                fontWeight = FontWeight.Bold,
                color = KnownKnownsTheme.colors.textStrong
            ),
            onHeightInflated = { height -> card4Height = height }
        )
        Card(
            modifier = Modifier
                .zIndex(3f)
                .offset(y = (166 - card3Offset).dp)
                .padding(horizontal = 32.dp),
            feed = news[2],
            cardColor = KnownKnownsTheme.colors.accentLemonYellow,
            topPadding = 20.dp,
            bottomPadding = 16.dp,
            textStyle = KnownKnownsTheme.typography.body16.copy(
                fontWeight = FontWeight.Bold,
                color = KnownKnownsTheme.colors.textStrong
            ),
            showKeyword = true,
            onHeightInflated = { height -> card3Height = height }
        )
        Card(
            modifier = Modifier
                .zIndex(4f)
                .offset(y = (166 - card2Offset).dp)
                .padding(horizontal = 16.dp),
            feed = news[1],
            cardColor = KnownKnownsTheme.colors.accentPink,
            topPadding = 20.dp,
            bottomPadding = 16.dp,
            textStyle = KnownKnownsTheme.typography.body18.copy(
                fontWeight = FontWeight.Bold,
                color = KnownKnownsTheme.colors.textStrong
            ),
            showKeyword = true,
            onHeightInflated = { height -> card2Height = height }
        )
        Card(
            modifier = Modifier
                .offset(y = (166 - card1Height).dp)
                .zIndex(5f),
            feed = news[0],
            cardColor = KnownKnownsTheme.colors.accentGreen,
            topPadding = 20.dp,
            bottomPadding = 16.dp,
            textStyle = KnownKnownsTheme.typography.body18.copy(
                fontWeight = FontWeight.Bold,
                color = KnownKnownsTheme.colors.textStrong
            ),
            showKeyword = true,
            onHeightInflated = { height -> card1Height = height }
        )
    }
}

/**
 * @param showKeyword 아래 세 개의 카드는 항상 키워드를 보여준다.
 */
@Composable
private fun Card(
    feed: NewsFeed,
    topPadding: Dp,
    bottomPadding: Dp,
    textStyle: TextStyle,
    cardColor: Color,
    modifier: Modifier = Modifier,
    showKeyword: Boolean = false,
    onHeightInflated: (height: Int) -> Unit,
) {
    val density = LocalDensity.current
    var lineCount by remember { mutableIntStateOf(2) }

    Box(
        modifier = modifier
            .height(166.dp)
            .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(color = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    onHeightInflated((layoutCoordinates.size.height / density.density).toInt())
                }
                .padding(horizontal = 20.dp)
                .padding(top = topPadding, bottom = bottomPadding),
        ) {
            Text(
                text = feed.title,
                style = textStyle,
                onTextLayout = { textLayoutResult ->
                    lineCount = textLayoutResult.lineCount
                }
            )
            if (showKeyword || lineCount == 1) {
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        feed.keyword,
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
                        text = feed.letter,
                        style = KnownKnownsTheme.typography.body13.copy(
                            fontWeight = FontWeight.Medium,
                            color = KnownKnownsTheme.colors.textStrong.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    KnownKnownsTheme {
        HomeScreen(
            news = persistentListOf(
                NewsFeed(
                    id = "1",
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com"
                ),
                NewsFeed(
                    id = "1",
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com"
                ),
                NewsFeed(
                    id = "1",
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com"
                ),
                NewsFeed(
                    id = "1",
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com"
                ),
                NewsFeed(
                    id = "1",
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com"
                ),
                NewsFeed(
                    id = "1",
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com"
                ),
            )
        )
    }
}