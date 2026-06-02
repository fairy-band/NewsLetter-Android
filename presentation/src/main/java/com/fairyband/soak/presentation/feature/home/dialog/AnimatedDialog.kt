package com.fairyband.soak.presentation.feature.home.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.fairyband.soak.core.extension.noRippleClickable

@Composable
fun AnimatedDialog(
    visibility: Boolean,
    onDismissRequest: () -> Unit,
    dimEnter: EnterTransition = fadeIn(tween(durationMillis = 200)),
    dimExit: ExitTransition = fadeOut(tween(durationMillis = 200)),
    contentEnter: EnterTransition = fadeIn(tween(durationMillis = 200)),
    contentExit: ExitTransition = fadeOut(tween(durationMillis = 200)),
    content: @Composable (onDismiss: () -> Unit) -> Unit,
) {
    if (visibility) {
        val visibleState = remember { MutableTransitionState(false) }
        val handleDismiss = {
            visibleState.targetState = false
        }

        LaunchedEffect(Unit) {
            visibleState.targetState = true
        }

        LaunchedEffect(
            visibleState.currentState,
            visibleState.targetState,
            visibleState.isIdle
        ) {
            if (visibleState.isIdle) {
                if (!visibleState.currentState && !visibleState.targetState) {
                    onDismissRequest()
                }
            }
        }

        Dialog(
            onDismissRequest = handleDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

                LaunchedEffect(dialogWindow) {
                    dialogWindow?.setDimAmount(0f)
                }

                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = dimEnter,
                    exit = dimExit,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = 0.7f))
                            .noRippleClickable(onClick = handleDismiss)
                    )
                }

                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = contentEnter,
                    exit = contentExit,
                ) {
                    content(handleDismiss)
                }
            }
        }
    }

}