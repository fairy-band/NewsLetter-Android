package com.fairyband.soak.presentation.feature.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.analytics.SoakAnalytics
import com.fairyband.soak.presentation.model.BookmarkFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import com.fairyband.soak.presentation.navigation.TabDestination
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookmarkScreen(viewModel: BookmarkViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cardColors = rememberBookmarkCardColors()

    // 탭을 벗어나면 컴포지션에서 빠지므로, 다시 들어올 때마다 최신 목록을 조회해요.
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    DarkSystemBar()

    BookmarkScreen(
        state = state,
        cardColors = cardColors,
        onTabClick = { tab ->
            SoakAnalytics.logBookmarkTabClick(tabName = tab.name.lowercase())
            viewModel.selectTab(tab)
        },
        onLoadMore = viewModel::loadMore,
        onLoadMoreShared = viewModel::loadMoreShared,
        onFeedClick = { index, feed ->
            SoakAnalytics.logBookmarkContentsDetailClick(
                cardIndex = index,
                contentTitle = feed.title,
                contentId = feed.exposureContentId.toString(),
            )
            navController.navigate(
                MainDestination.MarkdownDetail(
                    exposureContentId = feed.exposureContentId,
                    title = feed.letter,
                    pointColorArgb = cardColors[index % cardColors.size].pointColor.toArgb(),
                    fallbackUrl = feed.url,
                )
            )
        },
        onBookmarkClick = { feed ->
            SoakAnalytics.logBookmarkClick(
                isBookmarked = false,
                contentId = feed.exposureContentId.toString(),
                contentTitle = feed.title,
                screen = TabDestination.Bookmark.name,
            )
            viewModel.removeBookmark(feed)
        },
        onSharedBookmarkClick = { feed ->
            SoakAnalytics.logBookmarkClick(
                isBookmarked = feed.exposureContentId !in state.bookmarkedIds,
                contentId = feed.exposureContentId.toString(),
                contentTitle = feed.title,
                screen = TabDestination.Bookmark.name,
            )
            viewModel.toggleSharedBookmark(feed)
        },
    )
}

@Composable
private fun BookmarkScreen(
    state: BookmarkState,
    cardColors: List<BookmarkCardColor>,
    onTabClick: (BookmarkTab) -> Unit,
    onLoadMore: () -> Unit,
    onLoadMoreShared: () -> Unit,
    onFeedClick: (index: Int, feed: BookmarkFeed) -> Unit,
    onBookmarkClick: (BookmarkFeed) -> Unit,
    onSharedBookmarkClick: (BookmarkFeed) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        BookmarkTopTab(
            selectedTab = state.selectedTab,
            onTabClick = onTabClick,
        )

        when (state.selectedTab) {
            BookmarkTab.SAVED -> when {
                state.isEmpty -> EmptyBookmark(text = stringResource(R.string.bookmark_empty_saved))
                else -> BookmarkList(
                    feeds = state.feeds,
                    totalCount = state.totalCount.toInt(),
                    isBookmarked = { true },
                    cardColors = cardColors,
                    onLoadMore = onLoadMore,
                    onFeedClick = onFeedClick,
                    onBookmarkClick = onBookmarkClick,
                )
            }

            BookmarkTab.SHARED -> when {
                state.isSharedEmpty -> EmptyBookmark(text = stringResource(R.string.bookmark_empty_shared))
                else -> BookmarkList(
                    feeds = state.sharedFeeds,
                    totalCount = state.sharedTotalCount.toInt(),
                    isBookmarked = { it.exposureContentId in state.bookmarkedIds },
                    cardColors = cardColors,
                    onLoadMore = onLoadMoreShared,
                    onFeedClick = onFeedClick,
                    onBookmarkClick = onSharedBookmarkClick,
                )
            }
        }
    }
}

@Composable
private fun BookmarkTopTab(
    selectedTab: BookmarkTab,
    onTabClick: (BookmarkTab) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(SoakTheme.colors.borderPrimaryInverse)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            BookmarkTab.entries.forEach { tab ->
                BookmarkTopTabItem(
                    modifier = Modifier.weight(1f),
                    text = stringResource(tab.labelRes()),
                    isSelected = tab == selectedTab,
                    onClick = { onTabClick(tab) },
                )
            }
        }
    }
}

@Composable
private fun BookmarkTopTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = SoakTheme.typography.body16.copy(
                color = if (isSelected) SoakTheme.colors.textStrongInverse else SoakTheme.colors.iconTabInactive,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            ),
        )
        if (isSelected) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        color = SoakTheme.colors.textStrongInverse,
                        shape = RoundedCornerShape(1.dp),
                    )
            )
        }
    }
}

@Composable
private fun BookmarkList(
    feeds: List<BookmarkFeed>,
    totalCount: Int,
    isBookmarked: (BookmarkFeed) -> Boolean,
    cardColors: List<BookmarkCardColor>,
    onLoadMore: () -> Unit,
    onFeedClick: (index: Int, feed: BookmarkFeed) -> Unit,
    onBookmarkClick: (BookmarkFeed) -> Unit,
) {
    val lazyState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val threshold = 3
            val totalItemsCount = lazyState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - threshold
        }
    }

    LaunchedEffect(lazyState) {
        snapshotFlow { shouldLoadMore }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyState,
        contentPadding = PaddingValues(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "header") {
            ListHeader(
                modifier = Modifier.padding(bottom = 4.dp),
                totalCount = totalCount,
            )
        }
        itemsIndexed(items = feeds, key = { _, feed -> feed.exposureContentId }) { index, feed ->
            BookmarkCard(
                modifier = Modifier.animateItem(),
                feed = feed,
                isBookmarked = isBookmarked(feed),
                containerColor = cardColors[index % cardColors.size].containerColor,
                onClick = { onFeedClick(index, feed) },
                onBookmarkClick = { onBookmarkClick(feed) },
            )
        }
    }
}

@Composable
private fun ListHeader(
    totalCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.Bold)
        Text(
            text = stringResource(R.string.bookmark_total),
            style = style.copy(color = SoakTheme.colors.textStrongInverse),
        )
        Text(
            text = totalCount.toString(),
            style = style.copy(color = SoakTheme.colors.textDisabled),
        )
    }
}

@Composable
private fun BookmarkCard(
    feed: BookmarkFeed,
    isBookmarked: Boolean,
    containerColor: Color,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp)
            .background(color = containerColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(end = 38.dp),
                text = feed.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = SoakTheme.typography.body15.copy(
                    color = SoakTheme.colors.textStrong,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .height(16.dp)
                    .alpha(0.7f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val metaStyle = SoakTheme.typography.body13.copy(color = Color(0x80121212))
                Text(text = feed.keyword, style = metaStyle, maxLines = 1)
                VerticalDivider(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                    thickness = 1.dp,
                    color = Color(0xFF121212).copy(alpha = 0.1f),
                )
                Text(
                    text = feed.letter,
                    style = metaStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBookmarkClick,
                )
                // 북마크 리본이 카드 상단 가장자리에 붙도록 위쪽 여백은 두지 않아요.
                .padding(start = 12.dp, end = 24.dp, bottom = 12.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    if (isBookmarked) R.drawable.ic_bookmark_on else R.drawable.ic_bookmark_off
                ),
                contentDescription = stringResource(
                    if (isBookmarked) R.string.bookmark_remove_description else R.string.home_popup_bookmark_add_description
                ),
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun EmptyBookmark(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = SoakTheme.typography.body15.copy(color = SoakTheme.colors.iconTabInactive),
        )
    }
}

private data class BookmarkCardColor(
    val containerColor: Color,
    val pointColor: Color,
)

@Composable
private fun rememberBookmarkCardColors(): List<BookmarkCardColor> {
    val soakColors = SoakTheme.colors
    return remember(soakColors) {
        listOf(
            BookmarkCardColor(soakColors.blueBackgroundPrimary, soakColors.blueTextPrimary),
            BookmarkCardColor(soakColors.lemonYellowBackgroundPrimary, soakColors.lemonYellowTextPrimary),
            BookmarkCardColor(soakColors.purpleBackgroundPrimary, soakColors.purpleTextPrimary),
            BookmarkCardColor(soakColors.mintBackgroundPrimary, soakColors.mintTextPrimary),
            BookmarkCardColor(soakColors.pinkBackgroundPrimary, soakColors.pinkTextPrimary),
            BookmarkCardColor(soakColors.greenBackgroundPrimary, soakColors.greenTextPrimary),
        )
    }
}

private fun BookmarkTab.labelRes(): Int = when (this) {
    BookmarkTab.SAVED -> R.string.bookmark_tab_saved
    BookmarkTab.SHARED -> R.string.bookmark_tab_shared
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F)
@Composable
private fun BookmarkScreenPreview() {
    SoakTheme {
        BookmarkScreen(
            state = BookmarkState(
                isLoading = false,
                totalCount = 12,
                feeds = listOf(
                    BookmarkFeed(1, "네온, PostgreSQL 전문가도 놓친 치명적 실수", "PostgreSQL", "데브 위클리", ""),
                    BookmarkFeed(2, "Kotlin 객체 싱글톤, Gson 역직렬화에서 깨지는 이유", "Kotlin", "안드로이드 위클리", ""),
                    BookmarkFeed(3, "SwiftUI에서 달라진 NavigationStack 활용법", "Swift", "iOS 위클리", ""),
                ),
            ),
            cardColors = rememberBookmarkCardColors(),
            onTabClick = {},
            onLoadMore = {},
            onLoadMoreShared = {},
            onFeedClick = { _, _ -> },
            onBookmarkClick = {},
            onSharedBookmarkClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F)
@Composable
private fun BookmarkScreenEmptyPreview() {
    SoakTheme {
        BookmarkScreen(
            state = BookmarkState(isLoading = false),
            cardColors = rememberBookmarkCardColors(),
            onTabClick = {},
            onLoadMore = {},
            onLoadMoreShared = {},
            onFeedClick = { _, _ -> },
            onBookmarkClick = {},
            onSharedBookmarkClick = {},
        )
    }
}
