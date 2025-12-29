package com.fairyband.soak.presentation.feature.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.CARD_HEIGHT
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.SUMMARY_MAX_LINE
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.TITLE_MAX_LINE
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExploreScreen(viewModel: ExploreViewModel = koinViewModel()) {
    val navController = LocalNavController.current
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

    val feeds by viewModel.feeds.collectAsStateWithLifecycle()
    val lazyState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val threshold = 4
            val totalItemsCount = lazyState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            (lastVisibleItemIndex) >= totalItemsCount - threshold
        }
    }

    LaunchedEffect(lazyState) {
        snapshotFlow {
            shouldLoadMore
        }.distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadFeeds()
            }
    }

    DarkSystemBar()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(R.string.explore_count_of_articles, feeds.size),
            style = SoakTheme.typography.body14.copy(
                color = soakColors.textStrongInverse,
                fontWeight = FontWeight.Bold
            )
        )

        LazyVerticalGrid(
            state = lazyState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
        ) {
            items(feeds.size) { index ->
                Card(
                    modifier = Modifier.clickable {
                        navController.navigate(
                            MainDestination.ExploreDetail(
                                feeds = feeds,
                                index = index
                            )
                        )
                    },
                    content = feeds[index],
                    containerColor = cardColors[index % 6],
                )
            }
        }
    }
}

@Composable
private fun Card(
    content: ExploreFeed,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(176.dp)
            .background(
                shape = RoundedCornerShape(16.dp),
                color = containerColor
            )
            .padding(16.dp)
    ) {
        Text(
            content.title,
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
                content.keyword, style = SoakTheme.typography.body13.copy(
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
                content.letter, style = SoakTheme.typography.body13.copy(
                    color = Color(0x80121212)
                )
            )
        }
    }
}
