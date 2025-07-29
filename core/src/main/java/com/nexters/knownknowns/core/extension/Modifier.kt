package com.nexters.knownknowns.core.extension

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.bounceClick(
    scaleTo: Float = 1.03f,
    durationMillis: Int = 150,
    onClick: () -> Unit
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            scope.launch {
                scale.animateTo(
                    targetValue = scaleTo,
                    animationSpec = tween(durationMillis)
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis)
                )
                onClick()
            }
        }
}
