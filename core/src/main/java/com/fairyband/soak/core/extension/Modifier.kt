package com.fairyband.soak.core.extension

import android.os.VibrationEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}
fun Modifier.bounceClick(
    dialogPosition: Dp = 232.dp,
    durationMillis: Int = 900,
    onClick: () -> Unit,
): Modifier = composed {
    val context = LocalContext.current
    val density = LocalDensity.current
    val vibrator = context.vibrator
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(0f) }
    val firstTarget  = -500f
    var cardPosition by remember { mutableFloatStateOf(0f) }

    this
        .onGloballyPositioned {
            cardPosition = it.positionInWindow().y
        }
        .graphicsLayer {
            translationY = scale.value
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

                scale.animateTo(
                    targetValue = firstTarget,
                    animationSpec = tween(durationMillis)
                )

                val targetPx = with(density) { dialogPosition.toPx() }
                val secondTarget = targetPx - cardPosition   // 절대목표 - 현재절대 = 상대이동

                scale.animateTo(
                    targetValue = secondTarget,
                    animationSpec = tween(durationMillis)
                )

                onClick()
            }
        }
}
