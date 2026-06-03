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
import androidx.compose.foundation.layout.width
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
import com.fairyband.soak.presentation.feature.home.getCardColors
import com.fairyband.soak.presentation.model.NewsFeed
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
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
    onWebClick: (NewsFeed, Int) -> Unit,
    onShareClick: (Long, String, Color) -> Unit,
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
        val titleColors = getCardColors().map { it.titleColor }
        val item = cardItems[cardIndex]
        val titleColor = titleColors[cardIndex]

        BackHandler(visibility) {
            handleDismiss()
        }

        LaunchedEffect(visibility) {
            if (visibility) {
                Firebase.analytics.logEvent("impression_newsletter_carousel") {
                    param("object_section", "newsletter_card")
                    param("object_type", "newsletter")
                    param("object_id", item.title)
                    param("card_index", cardIndex.toLong())
                }
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
                    newsFeed = NewsFeed(
                        id = item.id,
                        title = item.title,
                        keyword = item.keyword,
                        letter = item.letter,
                        summary = item.summary,
                        url = item.url,
                        imageUrl = item.imageUrl,
                        language = item.language,
                        cardType = item.cardType,
                    ),
                    titleColor = titleColor,
                    onWebClick = { onWebClick(item, cardIndex) },
                    onShareClick = { onShareClick(item.id, item.title, titleColor) },
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
        }
    }
}

