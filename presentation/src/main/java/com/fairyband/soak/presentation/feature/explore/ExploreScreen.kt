package com.fairyband.soak.presentation.feature.explore

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.data.model.response.ExploreContentResponse
import com.fairyband.soak.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExploreScreen(viewModel: ExploreViewModel = koinViewModel()) {
    val news by viewModel.news.collectAsStateWithLifecycle()
    val soakColors = LocalSoakColors.current
    val cardColors = remember {
        listOf(
            soakColors.greenBackgroundPrimary,
            soakColors.pinkBackgroundPrimary,
            soakColors.lemonYellowBackgroundPrimary,
            soakColors.blueBackgroundPrimary,
            soakColors.orangeBackgroundPrimary,
            soakColors.purpleBackgroundPrimary,
        )
    }

    DarkSystemBar()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(R.string.explore_count_of_articles, news.size),
            style = SoakTheme.typography.body14.copy(
                color = soakColors.textStrongInverse,
                fontWeight = FontWeight.Bold
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
        ) {
            items(news.size) { index ->
                Card(news[index], cardColors[index % 6])
            }
        }
    }
}

@Composable
private fun Card(content: ExploreContentResponse, containerColor: Color) {
    Column(
        modifier = Modifier
            .height(176.dp)
            .background(
                shape = RoundedCornerShape(16.dp),
                color = containerColor
            )
            .padding(16.dp)
    ) {
        Text(
            content.provocativeHeadline,
            style = SoakTheme.typography.body15.copy(
                color = SoakTheme.colors.textStrong,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.height(18.dp)
        ) {
            Text(
                content.provocativeKeyword, style = SoakTheme.typography.body13.copy(
                    color = Color(0x80121212),
                    fontWeight = FontWeight.Bold,
                )
            )
            VerticalDivider(
                modifier = Modifier.padding(
                    vertical = 2.dp,
                    horizontal = 3.dp
                ),
                thickness = 1.dp,
                color = Color(0x80121212)
            )
            Text(
                content.newsletterName, style = SoakTheme.typography.body13.copy(
                    color = Color(0x80121212)
                )
            )
        }
    }
}

@Composable
private fun DarkSystemBar() {
    val view = LocalView.current
    val activity = LocalActivity.current

    DisposableEffect(Unit) {
        val window = activity?.window

        window?.let {
            val controller = WindowInsetsControllerCompat(window, view)
            controller.isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }

        onDispose {
            window?.let {
                val controller = WindowInsetsControllerCompat(window, view)
                controller.isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                    true
            }
        }
    }
}