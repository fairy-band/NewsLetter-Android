package com.fairyband.soak.core.extension

import android.os.VibrationEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.extension.BounceClickDefaults.CARD_HEIGHT
import com.fairyband.soak.core.extension.BounceClickDefaults.CARD_WIDTH_RATIO
import com.fairyband.soak.core.extension.BounceClickDefaults.INDICATOR_HEIGHT
import com.fairyband.soak.core.extension.BounceClickDefaults.MARGIN_CARD_TO_INDICATOR
import com.fairyband.soak.core.extension.ModifierDefaults.DURATION_MILLIS
import com.fairyband.soak.core.extension.ModifierDefaults.TARGET
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

private object BounceClickDefaults {
    const val CARD_WIDTH_RATIO = 0.8f
    val CARD_HEIGHT = 369.dp
    val MARGIN_CARD_TO_INDICATOR = 16.dp
    val INDICATOR_HEIGHT = 8.dp
}


fun Modifier.bounceClick(
    dialogVisible: Boolean,
    onClick: () -> Unit,
    onPromoteToFront: () -> Unit,
    onCardHidden: () -> Unit,
): Modifier = composed {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val density = LocalDensity.current

    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val cardWidth = screenWidth * CARD_WIDTH_RATIO

    val screenHeightPx = with(density) { screenHeight.toPx() }

    val topInsetPx = WindowInsets.statusBars.getTop(density)
    val bottomInsetPx = WindowInsets.navigationBars.getBottom(density)

    val groupHeightPx = with(density) {
        (CARD_HEIGHT + MARGIN_CARD_TO_INDICATOR + INDICATOR_HEIGHT).toPx()
    }
    val usableHeightPx = screenHeightPx - (topInsetPx + bottomInsetPx)
    val expectedTopPx = topInsetPx + (usableHeightPx - groupHeightPx) / 2f
    val expectedTopDp = with(density) { expectedTopPx.toDp() }

    val vibrator = context.vibrator
    val scope = rememberCoroutineScope()

    val translationYAnim = remember { Animatable(0f) }
    val scaleXAnim = remember { Animatable(1f) }
    val scaleYAnim = remember { Animatable(1f) }
    val alphaAnim = remember { Animatable(1f) }

    var cardPosition by remember { mutableFloatStateOf(0f) }
    var measuredWidth by remember { mutableFloatStateOf(0f) }
    var measuredHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(dialogVisible) {
        if (!dialogVisible) {
            alphaAnim.animateTo(
                targetValue = 1f,
            )

            coroutineScope {
                launch {
                    translationYAnim.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(DURATION_MILLIS)
                    )
                }
                launch {
                    scaleXAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(DURATION_MILLIS)
                    )
                }
                launch {
                    scaleYAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(DURATION_MILLIS)
                    )
                }
            }
        }
    }

    this
        .onGloballyPositioned { card ->
            cardPosition = card.positionInWindow().y
            measuredWidth = card.size.width.toFloat()
            measuredHeight = card.size.height.toFloat()
        }
        .graphicsLayer {
            translationY = translationYAnim.value
            scaleX = scaleXAnim.value
            scaleY = scaleYAnim.value
            alpha = alphaAnim.value
        }
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            scope.launch {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )

                // 1. 처음 올라가는 동작
                translationYAnim.animateTo(
                    targetValue = TARGET,
                    animationSpec = tween(900)
                )

                // 2. 카드 Z 위치 제일 앞으로 보내는 동작
                onPromoteToFront()

                val targetTopPx = with(density) { expectedTopDp.toPx() }
                val targetCenterPx = targetTopPx + with(density) { CARD_HEIGHT.toPx() } / 2f
                val currentCenterPx = cardPosition + (measuredHeight / 2f)
                val deltaY = targetCenterPx - currentCenterPx

                // 3. 지정된 크기로 맞춰지는 동작
                coroutineScope {
                    launch {
                        translationYAnim.animateTo(
                            targetValue = deltaY,
                            animationSpec = tween(DURATION_MILLIS)
                        )
                    }
                    launch {
                        scaleXAnim.animateTo(
                            targetValue = with(density) { cardWidth.toPx() } / measuredWidth,
                            animationSpec = tween(DURATION_MILLIS)
                        )
                    }
                    launch {
                        scaleYAnim.animateTo(
                            targetValue = with(density) { CARD_HEIGHT.toPx() } / measuredHeight,
                            animationSpec = tween(DURATION_MILLIS)
                        )
                    }
                    launch {
                        onCardHidden()
                    }
                }

                onClick()

                // 4. 사라지는 동작
                alphaAnim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(DURATION_MILLIS)
                )
            }
        }
}

object ModifierDefaults {
    const val TARGET = -500f
    const val DURATION_MILLIS = 700
}
