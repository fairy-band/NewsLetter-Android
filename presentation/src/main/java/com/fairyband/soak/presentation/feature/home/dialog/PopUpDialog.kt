package com.fairyband.soak.presentation.feature.home.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.SoakSnackbar
import com.fairyband.soak.presentation.analytics.SoakAnalytics
import com.fairyband.soak.presentation.analytics.toContentType
import com.fairyband.soak.presentation.feature.home.cardColorOf
import com.fairyband.soak.domain.model.NewsFeed
import kotlinx.collections.immutable.ImmutableList

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
    onDetailClick: (NewsFeed, Int) -> Unit,
    onShareClick: (NewsFeed, Color) -> Unit,
    bookmarkedIds: Set<Long>,
    onBookmarkClick: (NewsFeed) -> Unit,
    snackbarHostState: SnackbarHostState,
    cardItems: ImmutableList<NewsFeed>,
    cardIndex: Int,
) {
    AnimatedDialog(
        visibility = visibility,
        onDismissRequest = onDismissRequest,
        dimEnter = fadeIn(tween(delayMillis = 300, durationMillis = 200)),
        dimExit = fadeOut(tween(durationMillis = 0)),
        contentEnter = fadeIn(tween(delayMillis = 300, durationMillis = 200)),
        contentExit = fadeOut(tween(durationMillis = 0)),
    ) { handleDismiss ->
        val item = cardItems[cardIndex]
        val titleColor = cardColorOf(item.category).titleColor

        BackHandler(visibility) {
            handleDismiss()
        }

        LaunchedEffect(visibility) {
            if (visibility) {
                SoakAnalytics.logMainContentsDetailPageview(
                    cardIndex = cardIndex,
                    cardType = if (item.isTrending) "trending" else "recommend",
                    contentType = item.cardType.toContentType(),
                    contentTitle = item.title,
                    contentId = item.id.toString(),
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.weight(1f))
                PopUpItem(
                    modifier = Modifier.width(PopUpDialogDefaults.CARD_WIDTH),
                    newsFeed = item,
                    titleColor = titleColor,
                    onDetailClick = { onDetailClick(item, cardIndex) },
                    isBookmarked = item.id in bookmarkedIds,
                    onShareClick = { onShareClick(item, titleColor) },
                    onBookmarkClick = { onBookmarkClick(item) },
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_popup_dismiss),
                contentDescription = "pop up dismiss button",
                modifier = Modifier
                    .noRippleClickable(onClick = handleDismiss)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 124.dp),
            )
            // 다이얼로그는 별도 윈도우라 TabScreen의 스낵바가 dim 아래에 가려져요. 그래서 다이얼로그 안에 따로 띄워요.
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 56.dp),
                snackbar = { snackbarData ->
                    SoakSnackbar(message = snackbarData.visuals.message)
                },
            )
        }
    }
}

