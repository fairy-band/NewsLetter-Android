package com.fairyband.soak.presentation.feature.home

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil3.compose.AsyncImage
import com.fairyband.soak.core.designsystem.systembar.LightSystemBar
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.BuildConfig
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.bottomsheet.HomeBottomSheet
import com.fairyband.soak.presentation.feature.home.bottomsheet.NotificationBottomSheet
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialog
import com.fairyband.soak.presentation.model.NewsFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import com.fairyband.soak.presentation.analytics.SoakAnalytics
import com.fairyband.soak.presentation.analytics.toContentType
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val news by viewModel.news.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val hasRefreshedToday by viewModel.hasRefreshedToday.collectAsStateWithLifecycle()
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
            .collect { isBottomSheetVisible ->
                if (isBottomSheetVisible) {
                    SoakAnalytics.logBottomSheetCustomPageview()
                } else {
                    SoakAnalytics.logMainPageview()
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

    LightSystemBar()

    HomeScreen(
        onDismissRequest = {
            viewModel.onCardShown()
        },
        news = news,
        isRefreshing = isRefreshing,
        hasRefreshedToday = hasRefreshedToday,
        onRefresh = viewModel::refreshNews,
    )
}

@Composable
private fun HomeScreen(
    onDismissRequest: () -> Unit,
    news: ImmutableList<NewsFeed>,
    isRefreshing: Boolean,
    hasRefreshedToday: Boolean,
    onRefresh: () -> Unit,
) {
    var cardIndex: Int? by rememberSaveable { mutableStateOf(null) }
    val navController = LocalNavController.current
    val context = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { news.size })

    LaunchedEffect(Unit) {
        snapshotFlow { cardIndex }
            .map { it == null }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                SoakAnalytics.logMainPageview()
            }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Title()
//            Spacer(modifier = Modifier.height(102.dp))
            Cards(
                modifier = Modifier.fillMaxWidth(),
                pagerState = pagerState,
                news = news,
                onClick = { index ->
                    cardIndex = index
                },
                showPopup = cardIndex != null,
            )
            if (news.isNotEmpty()) {
                Spacer(modifier = Modifier.height(40.dp))
                Dots(
                    pagerState = pagerState,
                    count = news.size,
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            RefreshButton(
                isRefreshing = isRefreshing,
                hasRefreshedToday = hasRefreshedToday,
                onClick = onRefresh,
            )
        }
    }

    PopUpDialog(
        visibility = cardIndex != null,
        onDismissRequest = {
            cardIndex = null
            onDismissRequest()
        },
        onWebClick = { item, pageIndex ->
            navController.navigate(MainDestination.WebView(url = item.url))
            webClickEvent(item)
        },
        onShareClick = { id, title, color ->
            kakaoShare(
                id = id,
                title = title,
                color = color,
                context = context
            )
        },
        cardItems = news,
        cardIndex = cardIndex ?: 0,
    )
}

@Composable
private fun ColumnScope.Title() {
    val today = LocalDate.now()

    Text(
        modifier = Modifier
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

    Spacer(Modifier.height(12.dp))
    Text(
        text = stringResource(R.string.home_update_notice_b),
        style = SoakTheme.typography.body15.copy(
            fontWeight = FontWeight.SemiBold,
            color = SoakTheme.colors.textStrong,
        )
    )
    Spacer(Modifier.height(4.dp))
    RemainingTime()
}

@Composable
private fun RemainingTime() {
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

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(hh, style = numberStyle)
        Text(":", style = colonStyle)
        Text(mm, style = numberStyle)
        Text(":", style = colonStyle)
        Text(ss, style = numberStyle)
    }
}

@Composable
private fun ColumnScope.Cards(
    pagerState: PagerState,
    news: ImmutableList<NewsFeed>,
    onClick: (index: Int) -> Unit,
    showPopup: Boolean,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val cardColors = getCardColors()
    val screenWidthDp = with(density) { LocalWindowInfo.current.containerSize.width.toDp() }
    val screenHeight = with(density) { LocalWindowInfo.current.containerSize.height.toDp() }
    val cardWidth = 272.dp
    val cardHeight = 300.dp
    val focusedCardWidth by animateDpAsState(
        targetValue = if (showPopup) 300.dp else 272.dp,
        animationSpec = tween(durationMillis = 560),
    )
    val focusedCardHeight by animateDpAsState(
        targetValue = if (showPopup) 354.dp else 300.dp,
        animationSpec = tween(durationMillis = 560),
    )
    var yCoordinate by remember { mutableStateOf(0.dp) }
    val popupYOffset = (screenHeight - 354.dp) / 2 + 6.dp // FIXME: 6.dp는 임시 시각 보정 값. gesture hint 혹은 status bar padding에서 생긴 오차 같은데 연구 필요
    val yOffset by animateDpAsState(
        targetValue = if (showPopup) (popupYOffset - yCoordinate) else 0.dp,
        animationSpec = tween(durationMillis = 560),
    )
    val contentPaddingHorizontal = ((screenWidthDp - focusedCardWidth) / 2).coerceAtLeast(0.dp)
    val pageSpacing = (-80).dp

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = contentPaddingHorizontal),
        pageSpacing = pageSpacing,
        verticalAlignment = Alignment.Top,
    ) { page ->
        val rel = (pagerState.currentPage - page).toFloat() + pagerState.currentPageOffsetFraction
        val absRel = abs(rel)
        val scale = lerp(0.7f, 1.0f, 1f - absRel.coerceIn(0f, 1f))
        val isCurrentPage = page == pagerState.currentPage

        Card(
            modifier = Modifier
                .padding(top = 102.dp)
                .width(if (isCurrentPage) focusedCardWidth else cardWidth)
                .height(if (isCurrentPage) focusedCardHeight else cardHeight)
                .onGloballyPositioned { coordinates ->
                    if (yCoordinate == 0.dp) {
                        yCoordinate = with(density) {
                            coordinates.positionInRoot().y.toDp()
                        }
                    }
                }
                .offset(y = if (isCurrentPage) yOffset else 0.dp)
                .zIndex(1f - absRel)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            feed = news[page],
            cardColor = cardColors[page],
            onClick = {
                onClick(page)
            },
        )
    }
}

@Composable
private fun Card(
    feed: NewsFeed,
    cardColor: CardColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = cardColor.cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = feed.title,
                style = SoakTheme.typography.body18.copy(
                    fontWeight = FontWeight.Bold,
                    color = SoakTheme.colors.textStrong,
                ),
                maxLines = 2,
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = feed.keyword,
                    style = SoakTheme.typography.body14.copy(
                        fontWeight = FontWeight.Medium,
                        color = SoakTheme.colors.textStrong.copy(alpha = 0.4f),
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
                        color = SoakTheme.colors.textStrong.copy(alpha = 0.5f),
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(corner = CornerSize(16.dp)))
                    .background(color = Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (feed.imageUrl != null) {
                    AsyncImage(
                        model = feed.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = feed.cardType,
                            style = SoakTheme.typography.head28.copy(
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = cardColor.imageTextColor,
                            )
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(48.dp)
                                .height(4.dp)
                                .background(color = cardColor.imageTextColor)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Dots(
    pagerState: PagerState,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(count) { index ->
            val isActive = pagerState.currentPage == index
            val dotWidth by animateDpAsState(
                targetValue = if (isActive) 22.dp else 8.dp,
                animationSpec = tween(durationMillis = 280),
                label = "dot_width",
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(dotWidth)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        color = if (isActive) Color(0xFF121212) else Color.Black.copy(alpha = 0.18f)
                    )
            )
        }
    }
}

@Composable
private fun RefreshButton(
    isRefreshing: Boolean,
    hasRefreshedToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isEnabled = !isRefreshing && !hasRefreshedToday
    val contentColor =
        if (isEnabled) SoakTheme.colors.textSecondary else SoakTheme.colors.textDisabled

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(SoakTheme.colors.fillPrimary)
            .clickable(enabled = isEnabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 1.5.dp,
                color = contentColor,
            )
        } else {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
        }
        Text(
            text = "${stringResource(R.string.home_refresh_button_label)} ${if (hasRefreshedToday) 1 else 0}/1",
            style = SoakTheme.typography.body14.copy(
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
            ),
        )
    }
}

private fun buttonClickEvent(jobGroup: List<String>, careerLevel: String) {
    SoakAnalytics.logBottomSheetCustomClick(jobGroup = jobGroup, careerLevel = careerLevel)
}

private fun webClickEvent(item: NewsFeed) {
    SoakAnalytics.logMainContentsDetailClick(
        cardType = "recommend",
        contentType = item.cardType.toContentType(),
        contentTitle = item.title,
        contentId = item.id.toString(),
    )
}

private fun kakaoShare(
    id: Long,
    title: String,
    color: Color,
    context: Context
) {
    val templateId = 124946L
    val textColor = String.format("%06X", color.toArgb() and 0xFFFFFF)
    val templateArgs = mapOf(
        "TITLE" to title,
        "IMG" to "${BuildConfig.BASE_URL}/share/og?exposureContentId=$id&textColor=%23$textColor"
    )

    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        ShareClient.instance.shareCustom(
            context,
            templateId,
            templateArgs
        ) { sharingResult, error ->
            if (error != null) {
                Timber.e("카카오톡 공유 실패: ${error.message}")
            } else if (sharingResult != null) {
                context.startActivity(sharingResult.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    } else {
        val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId, templateArgs)
        try {
            val intent = CustomTabsIntent.Builder().build().intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = sharerUrl
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e("웹 공유 실패: ${e.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    SoakTheme {
        HomeScreen(
            onDismissRequest = {},
            isRefreshing = false,
            hasRefreshedToday = false,
            onRefresh = {},
            news = persistentListOf(
                NewsFeed(
                    id = 1,
                    title = "38800원은 너무 비싸",
                    keyword = "Kotlin",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com",
                    imageUrl = null,
                    language = "한국어",
                    cardType = "NEWS",
                ),
                NewsFeed(
                    id = 2,
                    title = "Jetpack Compose 최신 기능",
                    keyword = "Android",
                    letter = "Android Weekly",
                    summary = "",
                    url = "https://naver.com",
                    imageUrl = null,
                    language = "한국어",
                    cardType = "NEWS",
                ),
                NewsFeed(
                    id = 3,
                    title = "Kotlin 2.0 새로운 기능들",
                    keyword = "Kotlin",
                    letter = "Kotlin Weekly",
                    summary = "",
                    url = "https://naver.com",
                    imageUrl = null,
                    language = "한국어",
                    cardType = "NEWS",
                ),
                NewsFeed(
                    id = 4,
                    title = "AI가 바꾸는 개발 생태계",
                    keyword = "AI",
                    letter = "GeekNews",
                    summary = "",
                    url = "https://naver.com",
                    imageUrl = null,
                    language = "한국어",
                    cardType = "NEWS",
                ),
                NewsFeed(
                    id = 5,
                    title = "웹 성능 최적화 전략",
                    keyword = "Web",
                    letter = "Frontend Focus",
                    summary = "",
                    url = "https://naver.com",
                    imageUrl = null,
                    language = "한국어",
                    cardType = "NEWS",
                ),
                NewsFeed(
                    id = 6,
                    title = "도커와 쿠버네티스 실전 가이드",
                    keyword = "DevOps",
                    letter = "DevOps Weekly",
                    summary = "",
                    url = "https://naver.com",
                    imageUrl = null,
                    language = "한국어",
                    cardType = "NEWS",
                ),
            ),
        )
    }
}
