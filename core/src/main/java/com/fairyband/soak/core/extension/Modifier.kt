package com.fairyband.soak.core.extension

import android.content.Context
import android.os.VibrationEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext

object ModifierDefaults {
    const val DURATION_MILLIS = 700
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.bounceClick(
    isInvisible: Boolean,
    onClick: (xPx: Float, yPx: Float, widthPx: Float, heightPx: Float) -> Unit,
): Modifier = composed {
    val context = LocalContext.current

    var cardXPx by remember { mutableFloatStateOf(0f) }
    var cardYPx by remember { mutableFloatStateOf(0f) }
    var latestWidthPx by remember { mutableFloatStateOf(0f) }
    var latestHeightPx by remember { mutableFloatStateOf(0f) }

    this
        .onGloballyPositioned { card ->
            if (!isInvisible) {
                cardXPx = card.positionInWindow().x
                cardYPx = card.positionInWindow().y
                latestWidthPx = card.size.width.toFloat()
                latestHeightPx = card.size.height.toFloat()
            }
        }
        .graphicsLayer {
            alpha = if (isInvisible) 0f else 1f
        }
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            context.vibrator.vibrate(
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            )
            onClick(cardXPx, cardYPx, latestWidthPx, latestHeightPx)
        }
}

fun getStatusBarHeight(context: Context): Int {
    var statusbarHeight = 0
    val resourceStatusId: Int =
        context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceStatusId > 0) statusbarHeight =
        context.resources.getDimensionPixelSize(resourceStatusId)
    return statusbarHeight
}
