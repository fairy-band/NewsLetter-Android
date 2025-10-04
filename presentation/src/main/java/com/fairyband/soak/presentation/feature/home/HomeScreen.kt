package com.fairyband.soak.presentation.feature.home

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.fairyband.soak.core.extension.bounceClick
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.HomeDefaults.DRAWER_COLOR
import com.fairyband.soak.presentation.feature.home.HomeDefaults.DRAWER_HEIGHT
import com.fairyband.soak.presentation.feature.home.HomeDefaults.DRAWER_TO_CARD_MARGIN
import com.fairyband.soak.presentation.feature.home.HomeDefaults.FRONT_MOST_Z_INDEX
import com.fairyband.soak.presentation.feature.home.bottomsheet.HomeBottomSheet
import com.fairyband.soak.presentation.feature.home.bottomsheet.NotificationBottomSheet
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialog
import com.fairyband.soak.presentation.model.NewsFeed
import com.fairyband.soak.presentation.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                if (isHome) {
                    // 앱 메인 페이지 진입
                    Firebase.analytics.logEvent("pageview_main") {}
                } else {
                    // 맞춤정보 바텀시트 노출
                    Firebase.analytics.logEvent("pageview_bottom_sheet_custom") {
                        param("object_type", "bottom_sheet")
                    }
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

    val activity = LocalActivity.current
    var isWide = true

    if (activity != null) {
        val windowSizeClass = calculateWindowSizeClass(activity = activity)
        val devicePosture = rememberHomeState()
        val widthSizeClass = windowSizeClass.widthSizeClass

        isWide = widthSizeClass == WindowWidthSizeClass.Expanded && !devicePosture.value.isNormal
    }

    HomeScreen(
        onDismissRequest = {
            viewModel.onCardShown()
        },
        news = news,
        colorType = colorType,
        isWide = isWide,
    )
}

@Composable
private fun HomeScreen(
    onDismissRequest: () -> Unit,
    news: ImmutableList<NewsFeed>,
    colorType: String,
    isWide: Boolean,
) {
    var cardIndex: Int? by rememberSaveable { mutableStateOf(null) }
    var cardsHeight by remember { mutableStateOf(0.dp) }
    val drawerOffset = if (cardsHeight > 0.dp) {
        DRAWER_HEIGHT - (cardsHeight + DRAWER_TO_CARD_MARGIN)
    } else {
        0.dp
    }
    val navController = LocalNavController.current
    var onCardHidden by remember { mutableStateOf(false) }
    var dismissedCardIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        snapshotFlow { cardIndex }
            .map { it == null }
            .distinctUntilChanged()
            .collect { isHome ->
                if (isHome) {
                    // 앱 메인 페이지 진입
                    Firebase.analytics.logEvent("pageview_main") {}
                } else {
                    // 뉴스레터 캐러셀 진입
                    Firebase.analytics.logEvent("pageview_newsletter_carousel") {
                        param("object_type", "newsletter")
                    }
                }
            }
    }

    LaunchedEffect(cardIndex, news) {
        cardIndex?.let {
            // 뉴스레터 클릭
            Firebase.analytics.logEvent("click_main") {
                val title = news.getOrNull(it)?.title.orEmpty()

                param("object_section", "newsletter_list")
                param("object_type", "newsletter")
                param("object_id", title)
                param("list_index", it.toLong())
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_setting),
                contentDescription = "setting button",
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        end = 8.dp
                    )
                    .align(Alignment.End)
                    .noRippleClickable {
                        navController.navigate(Screen.Setting)
                    }
            )
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
                style = SoakTheme.typography.title.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                ),
                color = SoakTheme.colors.textStrong,
            )
            Timer()
            Spacer(modifier = Modifier.weight(1f))

            if (isWide) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_drawer_half),
                        contentDescription = "home drawer image",
                        modifier = Modifier
                            .offset(y = drawerOffset)
                            .drawBehind {
                                drawRect(
                                    color = DRAWER_COLOR,
                                    topLeft = Offset(x = 0f, y = size.height - 2f),
                                    size = Size(width = size.width, height = size.height)
                                )
                            },
                    )
                    Cards(
                        news = news,
                        onClick = { index ->
                            cardIndex = index
                            dismissedCardIndex = null
                        },
                        colorType = colorType,
                        onCardsHeight = { height ->
                            if (cardsHeight > 0.dp) return@Cards

                            cardsHeight = height.dp
                        },
                        modifier = Modifier.fillMaxWidth(0.5f),
                        dialogVisible = cardIndex != null,
                        onCardHidden = { onCardHidden = true },
                        dismissedCardIndex = dismissedCardIndex,
                        onDismissAnimationFinished = { dismissedCardIndex = null }
                    )
                }
            } else {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_drawer_half),
                        contentDescription = "home drawer image",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .offset(y = drawerOffset)
                            .drawBehind {
                                drawRect(
                                    color = DRAWER_COLOR,
                                    topLeft = Offset(x = 0f, y = size.height - 2f),
                                    size = Size(width = size.width, height = size.height)
                                )
                            },
                    )
                    Cards(
                        news = news,
                        onClick = { index ->
                            cardIndex = index
                            dismissedCardIndex = null
                        },
                        colorType = colorType,
                        onCardsHeight = { height ->
                            if (cardsHeight > 0.dp) return@Cards

                            cardsHeight = height.dp
                        },
                        dialogVisible = cardIndex != null,
                        onCardHidden = { onCardHidden = true },
                        dismissedCardIndex = dismissedCardIndex,
                        onDismissAnimationFinished = { dismissedCardIndex = null }
                    )
                }
            }

        }
    }

    PopUpDialog(
        visibility = cardIndex != null,
        backgroundVisibility = onCardHidden,
        onDismissRequest = {
            dismissedCardIndex = cardIndex
            cardIndex = null
            onDismissRequest()
            onCardHidden = false
        },
        cardItems = news,
        cardIndex = cardIndex ?: 0,
        colorType = colorType,
    )
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
        val suffixString = stringResource(R.string.home_limited_time_notice)
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
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = suffixString,
            style = SoakTheme.typography.body15.copy(
                fontWeight = FontWeight.Medium,
                color = SoakTheme.colors.textSecondary,
            )
        )
    }
}

@Composable
private fun Cards(
    dialogVisible: Boolean,
    news: ImmutableList<NewsFeed>,
    onClick: (Int) -> Unit,
    colorType: String,
    onCardsHeight: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onCardHidden: () -> Unit,
    dismissedCardIndex: Int?,
    onDismissAnimationFinished: () -> Unit,
) {
    val topPaddings = listOf(20.dp, 20.dp, 20.dp, 20.dp, 16.dp, 16.dp)
    val bottomPaddings = listOf(16.dp, 16.dp, 16.dp, 16.dp, 12.dp, 12.dp)
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

    var start by remember { mutableIntStateOf(0) }
    val mapFeedIndex = { index: Int -> (index - start + news.size) % news.size }

    val density = LocalDensity.current
    val stepDp = 106.dp
    val stepPx = with(density) { stepDp.toPx() }

    var scrollAccum by remember { mutableFloatStateOf(0f) }
    var progress by remember { mutableFloatStateOf(0f) }
    val scrollState = rememberScrollableState { delta ->
        scrollAccum += delta

        delta
    }

    LaunchedEffect(scrollAccum) {
        if (news.isEmpty()) return@LaunchedEffect

        if (scrollAccum > stepPx) {
            start = (start + 1) % news.size
            scrollAccum -= stepPx
        }

        if (scrollAccum < -stepPx) {
            start = (start - 1 + news.size) % news.size
            scrollAccum += stepPx
        }

        progress = scrollAccum / stepPx
    }

    LaunchedEffect(cardOffsets) {
        if (news.isEmpty()) return@LaunchedEffect

        onCardsHeight(cardOffsets[news.size - 1])
    }

    var frontMostIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(dialogVisible) {
        if (!dialogVisible) frontMostIndex = -1
    }

    val animationList = remember(news) {
        news.map { Animatable(initialValue = 0f) }
    }

    LaunchedEffect(news) {
        animationList.forEachIndexed { index, animatable ->
            delay(40L * index)
            launch {
                animatable.animateTo(
                    targetValue = -4f,
                    animationSpec = tween(durationMillis = 100)
                )
                animatable.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .scrollable(orientation = Orientation.Vertical, state = scrollState),
        contentAlignment = Alignment.BottomCenter
    ) {
        repeat(news.size) { index ->
            val feedIndex = mapFeedIndex(index)
            val baseZ = FRONT_MOST_Z_INDEX - feedIndex
            val currentZ = if (frontMostIndex == index) FRONT_MOST_Z_INDEX else baseZ

            Card(
                modifier = Modifier
                    .graphicsLayer {
                        if (progress < 0 && feedIndex == news.size - 1) {
                            val dy = cardHeights[feedIndex].toFloat() * progress * density.density
                            translationY = -dy
                        } else {
                            val dy =
                                cardHeights[(feedIndex + 1).coerceAtMost(news.size - 1)].toFloat() * progress * density.density
                            translationY = dy
                        }
                    }
                    .zIndex(currentZ)
                    .offset(y = (166 - cardOffsets[feedIndex]).dp)
                    .offset(y = animationList[feedIndex].value.dp)
                    .padding(horizontal = ((feedIndex - progress).coerceAtLeast(0f) * 16).dp),
                feed = news[index],
                cardColor = cardColors[index],
                topPadding = topPaddings[feedIndex],
                bottomPadding = bottomPaddings[feedIndex],
                textStyle = textStyles[feedIndex],
                showKeyword = keywordVisibilities[feedIndex],
                visibleHeight = if (feedIndex < 3) 106 else null,
                onHeightInflated = { height -> cardHeights[feedIndex] = height },
                onClick = { onClick(index) },
                onPromoteToFront = { frontMostIndex = index },
                onPromoteToBack = { frontMostIndex = -1 },
                onCardHidden = onCardHidden,
                isDismissing = dismissedCardIndex == index,
                onDismissAnimationFinished = onDismissAnimationFinished
            )
        }
        val density = LocalDensity.current.density

        // 아래로 스와이프할 때 가장 뒤쪽 카드가 서서히 올라와요.
        if (progress > 0) {
            val lastIndex = news.size - 1
            Card(
                modifier = Modifier
                    .graphicsLayer { translationY = -progress * cardHeights[lastIndex] * density }
                    .zIndex(-1f)
                    .offset(y = (166 - cardOffsets[(lastIndex - 1).coerceAtLeast(0)]).dp)
                    .padding(horizontal = ((lastIndex) * 16).dp),
                feed = news[start],
                cardColor = cardColors[start],
                topPadding = topPaddings[lastIndex],
                bottomPadding = bottomPaddings[lastIndex],
                textStyle = textStyles[lastIndex],
                showKeyword = keywordVisibilities[lastIndex],
                visibleHeight = null,
                onHeightInflated = { _ -> },
                onClick = { onClick(start) },
                onPromoteToFront = { frontMostIndex = start },
                onPromoteToBack = { frontMostIndex = -1 },
                onCardHidden = onCardHidden,
                isDismissing = dismissedCardIndex == start,
                onDismissAnimationFinished = onDismissAnimationFinished
            )
        }

        // 위로 스와이프할 때 가장 앞쪽 카드가 서서히 올라와요.
        // info: start - 1 은 마지막 index 이다.
        if (news.isNotEmpty() && progress < 0) {
            val risingIndex = (start - 1 + news.size) % news.size
            Card(
                modifier = Modifier
                    .graphicsLayer { translationY = progress * cardHeights[0] * density }
                    .zIndex(6f)
                    .offset(y = 166.dp),
                feed = news[risingIndex],
                cardColor = cardColors[risingIndex],
                topPadding = topPaddings[0],
                bottomPadding = bottomPaddings[0],
                textStyle = textStyles[0],
                showKeyword = keywordVisibilities[0],
                visibleHeight = 106,
                onHeightInflated = { _ -> },
                onClick = { onClick(risingIndex) },
                onPromoteToFront = { frontMostIndex = risingIndex },
                onPromoteToBack = { frontMostIndex = -1 },
                onCardHidden = onCardHidden,
                isDismissing = dismissedCardIndex == risingIndex,
                onDismissAnimationFinished = onDismissAnimationFinished
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
    visibleHeight: Int? = null,
    showKeyword: Boolean = false,
    onHeightInflated: (height: Int) -> Unit,
    onPromoteToFront: () -> Unit,
    onPromoteToBack: () -> Unit,
    onCardHidden: () -> Unit,
    isDismissing: Boolean,
    onDismissAnimationFinished: () -> Unit,
) {
    val density = LocalDensity.current
    var lineCount by remember { mutableIntStateOf(2) }

    Box(
        modifier = modifier
            .bounceClick(
                onClick = onClick,
                onPromoteToFront = onPromoteToFront,
                onPromoteToBack = onPromoteToBack,
                onCardHidden = onCardHidden,
                isDismissing = isDismissing,
                onDismissAnimationFinished = onDismissAnimationFinished
            )
            .height(166.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = cardColor)
    ) {
        val columnModifier = if (visibleHeight == null) {
            Modifier
        } else {
            Modifier.heightIn(visibleHeight.dp)
        }

        Column(
            modifier = columnModifier
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
                },
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
    Firebase.analytics.logEvent("click_bottom_sheet_custom") {
        param("object_type", "button")
        param("job_group", jobGroup.joinToString(separator = ","))
        param("career_level", careerLevel)
    }
}

private object HomeDefaults {
    val FRONT_MOST_Z_INDEX = 5f
    val DRAWER_HEIGHT = 527.dp
    val DRAWER_TO_CARD_MARGIN = 25.dp
    val DRAWER_COLOR = Color(0xFF99C9FF)
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
            isWide = false,
        )
    }
}
