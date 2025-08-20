package com.fairyband.soak.core.extension

import android.os.VibrationEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

fun Modifier.bounceClick(
    offset: Dp,
    dialogPosition: Dp = 232.dp,
    dialogHeight: Dp = 369.dp,
    dialogWidth: Dp = 314.dp,
    dialogVisible: Boolean,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource,
): Modifier = composed {
    val context = LocalContext.current
    val density = LocalDensity.current
    val vibrator = context.vibrator
    val scope = rememberCoroutineScope()

    val translationYAnim = remember { Animatable(0f) }
    val scaleXAnim = remember { Animatable(1f) }
    val scaleYAnim = remember { Animatable(1f) }

    var cardPosition by remember { mutableFloatStateOf(0f) }
    var measuredWidth by remember { mutableFloatStateOf(0f) }
    var measuredHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(dialogVisible) {
        if (!dialogVisible) {
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
        }
        .clickable(
            indication = null,
            interactionSource = interactionSource,
        ) {
            scope.launch {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )

                translationYAnim.animateTo(
                    targetValue = TARGET,
                    animationSpec = tween(DURATION_MILLIS)
                )

                val extraPadding = with(density) { offset.toPx() }

                val targetY = with(density) { dialogPosition.toPx() } - cardPosition
                val targetW = with(density) { dialogWidth.toPx() }
                val targetH = with(density) { dialogHeight.toPx() }

                val targetScaleX = targetW / measuredWidth
                val targetScaleY = targetH / measuredHeight

                coroutineScope {
                    launch {
                        translationYAnim.animateTo(
                            targetValue = targetY + extraPadding,
                            animationSpec = tween(DURATION_MILLIS)
                        )
                    }
                    launch {
                        scaleXAnim.animateTo(
                            targetValue = targetScaleX,
                            animationSpec = tween(DURATION_MILLIS)
                        )
                    }
                    launch {
                        scaleYAnim.animateTo(
                            targetValue = targetScaleY,
                            animationSpec = tween(DURATION_MILLIS)
                        )
                    }
                }

                onClick()
            }
        }
}

private object ModifierDefaults {
    const val TARGET = -500f
    const val DURATION_MILLIS = 900
}
