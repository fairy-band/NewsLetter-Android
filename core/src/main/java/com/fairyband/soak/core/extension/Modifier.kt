package com.fairyband.soak.core.extension

import android.content.Context
import android.os.VibrationEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
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

private object BounceClickDefaults {
    const val CARD_WIDTH_RATIO = 0.8f
    val CARD_HEIGHT = 369.dp
    val MARGIN_CARD_TO_INDICATOR = 16.dp
    val INDICATOR_HEIGHT = 8.dp
}

object ModifierDefaults {
    const val TARGET = -500f
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
    onClick: () -> Unit,
    onPromoteToFront: () -> Unit,
    onPromoteToBack: () -> Unit,
    onCardHidden: () -> Unit,
    isDismissing: Boolean,
    onDismissAnimationFinished: () -> Unit
): Modifier = composed {
    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()

    // 화면 및 목표 크기
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val targetCardWidth = screenWidth * CARD_WIDTH_RATIO
    val targetCardHeight = CARD_HEIGHT

    // 애니메이션 상태 관리
    val translationYAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(1f) }
    val widthAnim = remember { Animatable(0f) }
    val heightAnim = remember { Animatable(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    // 최신 측정 값
    var cardPosition by remember { mutableFloatStateOf(0f) }
    var latestWidthPx by remember { mutableFloatStateOf(0f) }
    var latestHeightPx by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isDismissing) {
        if (!isDismissing) return@LaunchedEffect

        isAnimating = true

        // 1. 가장 앞으로 Z축을 보내는 동작
        onPromoteToFront()

        alphaAnim.animateTo(1f)

        val backWidthDp = with(density) { latestWidthPx.toDp().value }
        val backHeightDp = with(density) { latestHeightPx.toDp().value }

        // 2. 원래의 크기로 돌아가는 동작
        coroutineScope {
            launch { translationYAnim.animateTo(TARGET, tween(DURATION_MILLIS)) }
            launch { widthAnim.animateTo(backWidthDp, tween(DURATION_MILLIS)) }
            launch { heightAnim.animateTo(backHeightDp, tween(DURATION_MILLIS)) }
        }

        // 3. 원래 Z축으로 돌아가는 동작
        onPromoteToBack()

        // 4. 원래 위치로 내려가는 동작
        translationYAnim.animateTo(0f, tween(DURATION_MILLIS))

        isAnimating = false

        onDismissAnimationFinished()
    }

    this
        .onGloballyPositioned { card ->
            if (!isAnimating) {
                cardPosition = card.positionInWindow().y
                latestWidthPx = card.size.width.toFloat()
                latestHeightPx = card.size.height.toFloat()
            }
        }
        .graphicsLayer {
            translationY = translationYAnim.value
            alpha = alphaAnim.value
        }
        .then(
            if (isAnimating) {
                Modifier.requiredSize(
                    width = widthAnim.value.dp,
                    height = heightAnim.value.dp
                )
            } else {
                Modifier
            }
        )
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            scope.launch {
                val screenHeightPx = with(density) { screenHeight.toPx() }
                val statusBarHeight = getStatusBarHeight(context)

                // 팝업 다이얼로그와 인디케이터를 포함한 높이
                val groupHeightPx = with(density) {
                    (targetCardHeight + MARGIN_CARD_TO_INDICATOR + INDICATOR_HEIGHT).toPx()
                }
                val targetTopPx = statusBarHeight + (screenHeightPx - groupHeightPx) / 2f
                val translationAmount = targetTopPx - cardPosition

                val startWidthDp = with(density) { latestWidthPx.toDp().value }
                val startHeightDp = with(density) { latestHeightPx.toDp().value }
                widthAnim.snapTo(startWidthDp)
                heightAnim.snapTo(startHeightDp)

                context.vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )

                isAnimating = true

                // 1. 처음 올라가는 동작
                translationYAnim.animateTo(TARGET, tween(900))

                // 2. 카드 Z 위치 제일 앞으로 보내는 동작
                onPromoteToFront()

                // 3. 지정된 크기로 맞춰지는 동작
                coroutineScope {
                    launch {
                        translationYAnim.animateTo(
                            -TARGET + translationAmount,
                            tween(DURATION_MILLIS)
                        )
                    }
                    launch { widthAnim.animateTo(targetCardWidth.value, tween(DURATION_MILLIS)) }
                    launch { heightAnim.animateTo(targetCardHeight.value, tween(DURATION_MILLIS)) }
                    launch { onCardHidden() }
                }

                onClick()

                // 4. 사라지는 동작
                alphaAnim.animateTo(0f, tween(DURATION_MILLIS))

                isAnimating = false
            }
        }
}

private fun getStatusBarHeight(context: Context): Int {
    var statusbarHeight = 0
    val resourceStatusId: Int =
        context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceStatusId > 0) statusbarHeight =
        context.resources.getDimensionPixelSize(resourceStatusId)

    return statusbarHeight
}
