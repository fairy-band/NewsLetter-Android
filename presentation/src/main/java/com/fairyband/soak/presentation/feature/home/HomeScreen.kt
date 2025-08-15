package com.fairyband.soak.presentation.feature.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.fairyband.soak.core.extension.bounceClick
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.bottomsheet.HomeBottomSheet
import com.fairyband.soak.presentation.feature.home.bottomsheet.NotificationBottomSheet
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialog
import com.fairyband.soak.presentation.model.NewsFeed
import com.fairyband.soak.presentation.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val news by viewModel.news.collectAsStateWithLifecycle()
    val colorType by viewModel.cardColorType.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasNotificationPermission by remember { mutableStateOf(false) }
    var bottomSheetVisibility by remember { mutableStateOf(false) }
    val showNotificationBottomSheet by
    viewModel.shouldShowNotificationSetting.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.eventFlow, lifecycleOwner) {
        viewModel.eventFlow.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is HomeSideEffect.ShowBottomSheet -> {
                        bottomSheetVisibility = true
                    }
                }
            }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetVisibility }
            .collect { isHome ->
                val screenName = if (isHome) Screen.Home.name else Screen.BottomSheetCustom.name

                // 앱 메인 페이지 진입 & 맞춤정보 바텀시트 노출
                Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                }
            }
    }

    LaunchedEffect(showNotificationBottomSheet) {
        hasNotificationPermission =
            NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    if (showNotificationBottomSheet && !hasNotificationPermission) {
        NotificationBottomSheet(
            onDismissRequest = viewModel::disableNotificationSetting,
        )
    }

    if (!showNotificationBottomSheet && bottomSheetVisibility) {
        HomeBottomSheet(
            onDismissRequest = {
                bottomSheetVisibility = false
            },
            onButtonClick = { preferences, workingExperience ->
                viewModel.saveUserInfo(
                    preferences = preferences,
                    workingExperience = workingExperience
                )

                buttonClickEvent(jobGroup = preferences, careerLevel = workingExperience)
            }
        )
    }

    HomeScreen(
        onDismissRequest = {
            viewModel.onCardShown()
        },
        news = news,
        colorType = colorType,
    )
}

@Composable
private fun HomeScreen(
    onDismissRequest: () -> Unit,
    news: ImmutableList<NewsFeed>,
    colorType: String,
) {
    var cardIndex: Int? by remember { mutableStateOf(null) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    LaunchedEffect(Unit) {
        snapshotFlow { cardIndex }
            .map { it == null }
            .distinctUntilChanged()
            .collect { isHome ->
                val screenName = if (isHome) Screen.Home.name else Screen.NewsLetterCarousel.name

                // 앱 메인 페이지 진입 & 뉴스레터 캐러셀 진입
                Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                }
            }
    }

    LaunchedEffect(cardIndex, news) {
        cardIndex?.let {
            // 뉴스레터 클릭
            Firebase.analytics.logEvent("click") {
                val objectId = news.getOrNull(it)?.id.orEmpty()

                param("navigation", Screen.Home.name)
                param("object_section", "newsletter_list")
                param("object_type", "newsletter")
                param("object_id", objectId)
                param("list_index", it.toLong())
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box {
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
                    style = SoakTheme.typography.title.copy(textAlign = TextAlign.Center),
                    color = SoakTheme.colors.textStrong,
                )
                Timer()
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_drawer),
                        contentDescription = "home drawer image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(screenWidth)
                            .offset(y = 60.dp)
                    )
                    Cards(
                        news = news,
                        onClick = { index ->
                            cardIndex = index
                        },
                        colorType = colorType,
                    )
                }
            }
        }

        PopUpDialog(
            visibility = cardIndex != null,
            onDismissRequest = {
                cardIndex = null
                onDismissRequest()
            },
            cardItems = news,
            cardIndex = cardIndex ?: 0,
            colorType = colorType,
        )
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
        val numberStyle = SoakTheme.typography.body16.copy(
            fontWeight = FontWeight.SemiBold,
            color = SoakTheme.colors.stateNegativePrimary
        )
        val colonStyle = SoakTheme.typography.body15.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.stateNegativePrimary
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
    onClick: (Int) -> Unit,
    colorType: String,
    modifier: Modifier = Modifier
) {
    val topPaddings = listOf(20.dp, 20.dp, 20.dp, 20.dp, 16.dp, 16.dp)
    val bottomPaddings = listOf(16.dp, 16.dp, 16.dp, 16.dp, 12.dp, 12.dp)
    val horizontalPaddings = listOf(0.dp, 16.dp, 32.dp, 48.dp, 64.dp, 80.dp)
    val keywordVisibilities = listOf(true, true, true, false, false, false)
    val textStyles = listOf(
        SoakTheme.typography.body18.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.textStrong
        ),
        SoakTheme.typography.body18.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.textStrong
        ),
        SoakTheme.typography.body16.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.textStrong
        ),
        SoakTheme.typography.body15.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.textStrong
        ),
        SoakTheme.typography.body14.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.textStrong
        ),
        SoakTheme.typography.body13.copy(
            fontWeight = FontWeight.Bold,
            color = SoakTheme.colors.textStrong
        )
    )
    val cardHeights = remember { mutableStateListOf(0, 0, 0, 0, 0, 0) }
    val cardOffsets by remember {
        derivedStateOf {
            buildList {
                var cumulative = 0
                for (height in cardHeights) {
                    cumulative += height
                    add(cumulative)
                }
            }
        }
    }
    val keywords = news.map { it.keyword }
    val cardColors = remember(news, colorType) { getCardColors(colorType, keywords) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        // 항상 6
        repeat(news.size) { index ->
            Card(
                modifier = Modifier
                    .zIndex(5f - index)
                    .offset(y = (166 - cardOffsets[index]).dp)
                    .padding(horizontal = horizontalPaddings[index]),
                feed = news[index],
                cardColor = cardColors[index],
                topPadding = topPaddings[index],
                bottomPadding = bottomPaddings[index],
                textStyle = textStyles[index],
                showKeyword = keywordVisibilities[index],
                onHeightInflated = { height -> cardHeights[index] = height },
                onClick = { onClick(index) },
            )
        }
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showKeyword: Boolean = false,
    onHeightInflated: (height: Int) -> Unit,
) {
    val density = LocalDensity.current
    var lineCount by remember { mutableIntStateOf(2) }

    Box(
        modifier = modifier
            .bounceClick(onClick = onClick)
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
                        style = SoakTheme.typography.body13.copy(
                            fontWeight = FontWeight.Medium,
                            color = SoakTheme.colors.textStrong.copy(alpha = 0.5f)
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
                        style = SoakTheme.typography.body13.copy(
                            fontWeight = FontWeight.Medium,
                            color = SoakTheme.colors.textStrong.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}

private fun buttonClickEvent(jobGroup: List<String>, careerLevel: String) {
    // 맞춤정보 바텀시트_맞춤정보 보기 버튼 클릭
    Firebase.analytics.logEvent("click") {
        param("navigation", Screen.BottomSheetCustom.name)
        param("object_type", "button")
        param("job_group", jobGroup.joinToString(separator = ","))
        param("career_level", careerLevel)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    SoakTheme {
        HomeScreen(
            onDismissRequest = {},
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
            ),
            colorType = "B",
        )
    }
}
