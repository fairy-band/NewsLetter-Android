package com.fairyband.soak.presentation.feature.home.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.getCardTitleColors
import com.fairyband.soak.presentation.model.NewsFeed
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged

internal object PopUpDialogDefaults {
    const val SUMMARY_MAX_LINE = 8
    const val TITLE_MAX_LINE = 2
    val CARD_WIDTH = 300.dp
    val CARD_HEIGHT = 354.dp
}

@Composable
internal fun PopUpDialog(
    visibility: Boolean,
    onDismissRequest: () -> Unit,
    onWebClick: (NewsFeed, Int) -> Unit,
    onShareClick: (Long, String, Color) -> Unit,
    cardItems: ImmutableList<NewsFeed>,
    cardIndex: Int,
    colorType: String,
    cardStartXPx: Float = 0f,
    cardStartYPx: Float = 0f,
    cardStartWidthPx: Float = 0f,
    cardStartHeightPx: Float = 0f,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val popupCardWidthPx = with(density) { PopUpDialogDefaults.CARD_WIDTH.toPx() }
    val popupCardHeightPx = with(density) { PopUpDialogDefaults.CARD_HEIGHT.toPx() }

    // Freeze start transform at open time so exit animation returns to same position
    var frozenOffsetX by remember { mutableFloatStateOf(0f) }
    var frozenOffsetY by remember { mutableFloatStateOf(0f) }
    var frozenScaleX by remember { mutableFloatStateOf(1f) }
    var frozenScaleY by remember { mutableFloatStateOf(1f) }
    var shouldRender by remember { mutableStateOf(false) }

    LaunchedEffect(visibility) {
        if (visibility) {
            val cardCenterX = cardStartXPx + cardStartWidthPx / 2f
            val cardCenterY = cardStartYPx + cardStartHeightPx / 2f
            frozenOffsetX = cardCenterX - screenWidthPx / 2f
            frozenOffsetY = cardCenterY - screenHeightPx / 2f
            frozenScaleX = if (popupCardWidthPx > 0f && cardStartWidthPx > 0f) cardStartWidthPx / popupCardWidthPx else 1f
            frozenScaleY = if (popupCardHeightPx > 0f && cardStartHeightPx > 0f) cardStartHeightPx / popupCardHeightPx else 1f
            shouldRender = true
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (visibility) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "popupProgress",
        finishedListener = { value -> if (value == 0f) shouldRender = false },
    )

    if (!shouldRender) return

    BackHandler(visibility) { onDismissRequest() }

    val keywords = cardItems.map { it.keyword }
    val titleColors = remember(cardItems, colorType) { getCardTitleColors(colorType, keywords) }
    val pageSize = PopUpDialogDefaults.CARD_WIDTH
    val horizontalPadding = (configuration.screenWidthDp.dp - pageSize) / 2

    Box {
        // Background overlay — fade only
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.7f * animatedProgress))
                .noRippleClickable(onClick = onDismissRequest)
        )

        // Card content — scale + translate from card position to center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = animatedProgress
                    translationX = frozenOffsetX * (1f - animatedProgress)
                    translationY = frozenOffsetY * (1f - animatedProgress)
                    scaleX = lerp(frozenScaleX, 1f, animatedProgress)
                    scaleY = lerp(frozenScaleY, 1f, animatedProgress)
                }
        ) {
            val pagerState = rememberPagerState(
                pageCount = { cardItems.size },
                initialPage = cardIndex,
            )

            LaunchedEffect(Unit) {
                val loggedPages = mutableSetOf<Int>()
                snapshotFlow { pagerState.currentPage }
                    .distinctUntilChanged()
                    .collect { page ->
                        if (loggedPages.add(page)) {
                            val item = cardItems[page]
                            Firebase.analytics.logEvent("impression_newsletter_carousel") {
                                param("object_section", "newsletter_card")
                                param("object_type", "newsletter")
                                param("object_id", item.title)
                                param("card_index", page.toLong())
                            }
                        }
                    }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.weight(1f))
                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fixed(pageSize),
                    pageSpacing = 12.dp,
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    key = { cardItems[it].id },
                ) { pageIndex ->
                    val item = cardItems[pageIndex]
                    PopUpItem(
                        newsFeed = NewsFeed(
                            id = item.id,
                            title = item.title,
                            keyword = item.keyword,
                            letter = item.letter,
                            summary = item.summary,
                            url = item.url,
                            imageUrl = item.imageUrl,
                            language = item.language,
                        ),
                        titleColor = titleColors[pageIndex],
                        onWebClick = { onWebClick(item, pageIndex) },
                        onShareClick = { onShareClick(item.id, item.title, titleColors[pageIndex]) },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Indicator(
                    pageCount = cardItems.size,
                    pageIndex = pagerState.currentPage,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_popup_dismiss),
                contentDescription = "pop up dismiss button",
                modifier = Modifier
                    .noRippleClickable(onClick = onDismissRequest)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 124.dp),
            )
        }
    }
}

@Composable
private fun Indicator(
    pageCount: Int,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == pageIndex
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(8.dp)
                    .background(
                        SoakTheme.colors.backgroundBase.copy(alpha = if (isSelected) 1f else 0.3f)
                    )
            )
        }
    }
}
