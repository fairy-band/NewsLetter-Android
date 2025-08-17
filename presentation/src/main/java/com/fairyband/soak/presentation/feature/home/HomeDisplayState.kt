package com.fairyband.soak.presentation.feature.home

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker

data class HomeDisplayState(
    val isNormal: Boolean = false,
    val isFlat: Boolean = false
)

@Composable
fun rememberHomeState(): State<HomeDisplayState> {
    val context = LocalContext.current
    val activity = context as? Activity
    val postureState = remember { mutableStateOf(HomeDisplayState()) }

    if (activity != null) {
        LaunchedEffect(activity) {
            WindowInfoTracker.getOrCreate(context)
                .windowLayoutInfo(activity)
                .collect { layoutInfo ->
                    val foldingFeature =
                        layoutInfo.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
                    when {
                        foldingFeature == null -> postureState.value = HomeDisplayState()

                        isHalfOpen(foldingFeature) -> postureState.value =
                            HomeDisplayState(isNormal = true, isFlat = true)

                        isFlat(foldingFeature) -> postureState.value = HomeDisplayState(isFlat = true)
                    }
                }
        }
    }

    return postureState
}

private fun isHalfOpen(foldFeature: FoldingFeature?): Boolean =
    foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.VERTICAL

private fun isFlat(foldFeature: FoldingFeature?): Boolean =
    foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
