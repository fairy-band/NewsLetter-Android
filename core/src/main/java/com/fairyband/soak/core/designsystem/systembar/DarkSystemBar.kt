package com.fairyband.soak.core.designsystem.systembar

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun DarkSystemBar() {
    val view = LocalView.current
    val activity = LocalContext.current as? Activity

    SideEffect {
        val window = activity?.window

        window?.let {
            val controller = WindowInsetsControllerCompat(window, view)
            controller.isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }
}