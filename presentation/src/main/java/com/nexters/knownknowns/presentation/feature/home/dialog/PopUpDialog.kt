package com.nexters.knownknowns.presentation.feature.home.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.nexters.knownknowns.core.extension.noRippleClickable
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.R
import com.nexters.knownknowns.presentation.feature.home.dialog.PopUpDialogDefaults.CARD_WIDTH_RATIO
import com.nexters.knownknowns.presentation.model.NewsFeed
import kotlinx.collections.immutable.ImmutableList

internal object PopUpDialogDefaults {
    const val SUMMARY_MAX_LINE = 8
    const val TITLE_MAX_LINE = 2
    const val CARD_WIDTH_RATIO = 0.8f
}

@Composable
internal fun PopUpDialog(
    visibility: Boolean,
    onDismissRequest: () -> Unit,
    cardItems: ImmutableList<NewsFeed>,
    cardIndex: Int,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val pageSize = screenWidth * CARD_WIDTH_RATIO
    val horizontalPadding = (screenWidth - pageSize) / 2

    val pagerState = rememberPagerState(
        pageCount = { cardItems.size },
        initialPage = cardIndex
    )

    Box {
        AnimatedVisibility(
            visibility,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.7f))
                    .noRippleClickable(onClick = onDismissRequest)
            )
        }

        AnimatedVisibility(
            visibility,
            enter = fadeIn(animationSpec = tween(150, 200)),
            exit = fadeOut(animationSpec = tween(200)),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(217f))
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
                            url = item.url
                        ),
                        titleColor = KnownKnownsTheme.colors.statePositivePrimary, // TODO: 색상 분기처리하기 by 이유빈
                        onClick = onDismissRequest,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Indicator(
                    pageCount = cardItems.size,
                    pageIndex = pagerState.currentPage
                )
                Spacer(modifier = Modifier.height(49.dp))
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_popup_dismiss),
                    contentDescription = "pop up dismiss button",
                    modifier = Modifier.noRippleClickable(onClick = onDismissRequest)
                )
                Spacer(modifier = Modifier.weight(124f))
            }
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
                        KnownKnownsTheme.colors.backgroundBase.copy(alpha = if (isSelected) 1f else 0.3f)
                    )
            )
        }
    }
}
