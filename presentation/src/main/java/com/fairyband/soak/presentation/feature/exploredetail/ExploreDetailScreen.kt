package com.fairyband.soak.presentation.feature.exploredetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.CARD_HEIGHT
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.SUMMARY_MAX_LINE
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.TITLE_MAX_LINE
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExploreDetailScreen(
    viewModel: ExploreDetailViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val soakColors = LocalSoakColors.current
    val titleColors = remember {
        listOf(
            soakColors.greenText,
            soakColors.pinkText,
            soakColors.lemonYellowText,
            soakColors.blueText,
            soakColors.orangeText,
            soakColors.purpleText,
        )
    }

    val feeds by viewModel.feeds.collectAsStateWithLifecycle()
    val index by viewModel.selectedIndex.collectAsStateWithLifecycle()

    val lazyState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val threshold = 4
            val totalItemsCount = lazyState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            (lastVisibleItemIndex) >= totalItemsCount - threshold
        }
    }

    LaunchedEffect(lazyState) {
        snapshotFlow {
            shouldLoadMore
        }.distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadFeeds()
            }
    }

    DetailBackground()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(
                onClick = {
                    navController.pop()
                },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = SoakTheme.colors.iconStrongInverse,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 88.dp)
                .background(
                    color = SoakTheme.colors.backgroundBase,
                    shape = RoundedCornerShape(16.dp),
                )
                .width(300.dp)
                .height(CARD_HEIGHT)
                .padding(24.dp),
        ) {
            val titleColor = titleColors[index % 6]
            val feed = feeds[index]
            var titleLineCount by remember { mutableIntStateOf(1) }

            Text(
                text = feed.title,
                style = SoakTheme.typography.head20.copy(fontWeight = FontWeight.Bold),
                color = titleColor,
                maxLines = TITLE_MAX_LINE,
                onTextLayout = { textLayout ->
                    titleLineCount = textLayout.lineCount
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = feed.keyword,
                    style = SoakTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                    color = titleColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                VerticalDivider(color = titleColor)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = feed.letter,
                    style = SoakTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                    color = titleColor
                )
            }
            Spacer(modifier = Modifier.height(if (titleLineCount == TITLE_MAX_LINE) 16.dp else 44.dp))
            Text(
                text = feed.summary,
                style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.Normal),
                color = SoakTheme.colors.textPrimary,
                maxLines = SUMMARY_MAX_LINE,
                minLines = SUMMARY_MAX_LINE,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                paddingVertical = 12.dp,
                onClick = {
                    navController.navigate(MainDestination.WebView(url = feed.url))
                    webClickEvent(id = feed.id, index = index)
                },
                shape = CircleShape,
                borderWidth = 1.dp,
                borderColor = SoakTheme.colors.borderSecondary
            ) {
                Text(
                    text = stringResource(id = R.string.home_popup_button_text),
                    style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 56.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.explore_count_of_articles, feeds.size),
                style = SoakTheme.typography.body14.copy(
                    fontWeight = FontWeight.Bold,
                    color = SoakTheme.colors.textStrongInverse
                )
            )
            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                state = lazyState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(count = feeds.size, key = { index -> feeds[index].id }) { index ->
                    Feed(feeds[index])
                }
            }
        }
    }
}

@Composable
private fun DetailBackground() {
    DarkSystemBar()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.home_drawer_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 84.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .blur(20.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SoakTheme.colors.backgroundSurfaceInverse,
                            Color.Transparent
                        )
                    )
                )

        )
    }
}

@Composable
private fun Feed(feed: ExploreFeed) {
    Box(
        modifier = Modifier
            .size(width = 166.dp, height = 98.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color.Black.copy(alpha = 0.1f))
            .border(shape = RoundedCornerShape(16.dp), width = 1.dp, color = Color.White),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = feed.title,
            style = SoakTheme.typography.body15.copy(
                color = SoakTheme.colors.textStrongInverse,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

private fun webClickEvent(id: Int, index: Int) {
    // 카드 내 ‘원문 보기’ 버튼 클릭
    Firebase.analytics.logEvent("click_newsletter_carousel") {
        param("object_section", "newsletter_card")
        param("object_type", "button")
        param("object_id", id.toString())
        param("card_index", "explore-${index}")
    }
}