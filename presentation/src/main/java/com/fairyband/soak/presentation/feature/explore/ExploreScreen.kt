package com.fairyband.soak.presentation.feature.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.LocalSnackbarController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.explore.bottomsheet.ReportNewsletterBottomSheet
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExploreScreen(viewModel: ExploreViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val soakColors = LocalSoakColors.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarController = LocalSnackbarController.current
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

    val state by viewModel.state.collectAsStateWithLifecycle()
    val lazyState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val threshold = 4
            val totalItemsCount = lazyState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItemIndex >= totalItemsCount - threshold
        }
    }
    val feeds = state.feeds
    val totalCount = state.totalCount

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    val reportSuccessMessage = stringResource(R.string.explore_report_success)

    LaunchedEffect(lazyState) {
        snapshotFlow {
            shouldLoadMore
        }.distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadFeeds()
            }
    }

    LaunchedEffect(viewModel.eventFlow, lifecycleOwner) {
        viewModel.eventFlow.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    is ExploreSideEffect.ShowReportComplete -> {
                        snackbarController.showSnackbar(reportSuccessMessage)
                        viewModel.resetReportState()
                        showBottomSheet = false
                    }
                }
            }
    }

    DarkSystemBar()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.explore_count_of_articles, totalCount),
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
                ),
            ) {
                items(count = feeds.size, key = { index -> feeds[index].id }) { index ->
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

        ReportFab(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 16.dp,
                    bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding(),
                ),
            onClick = { showBottomSheet = true },
        )
    }

    if (showBottomSheet) {
        ReportNewsletterBottomSheet(
            name = state.name,
            url = state.url,
            selectedPreferences = state.selectedPreferences,
            language = state.language,
            updateName = viewModel::updateName,
            updateUrl = viewModel::updateUrl,
            updateLanguage = viewModel::updateLanguage,
            updatePreference = viewModel::updatePreference,
            isSubmitEnabled = state.isSubmitEnabled,
            onDismissRequest = {
                showBottomSheet = false
                viewModel.resetReportState()
            },
            onSubmit = {
                viewModel.reportNewsletter()
                // 성공 후 onDissmiss 처리
            },
        )
    }
}

@Composable
private fun ReportFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            // 최신 버전에서는 dropShadow 를 써야 하나, 현재 라이브러리에서는 지원 X
            .shadow(elevation = 8.dp, shape = CircleShape)
            .background(color = SoakTheme.colors.fillWhite, shape = CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = null,
            tint = SoakTheme.colors.iconSecondary,
        )
        Text(
            text = stringResource(R.string.explore_report_fab),
            style = SoakTheme.typography.body14.copy(
                color = SoakTheme.colors.textSecondary,
                fontWeight = FontWeight.SemiBold,
            ),
        )
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
                    fontWeight = FontWeight.Medium,
                )
            )
            VerticalDivider(
                modifier = Modifier.padding(
                    vertical = 2.dp,
                    horizontal = 3.dp
                ),
                thickness = 1.dp,
                color = Color(0xFF121212).copy(alpha = 0.1f)
            )
            Text(
                content.letter, style = SoakTheme.typography.body13.copy(
                    color = Color(0x80121212),
                    fontWeight = FontWeight.Medium,
                )
            )
        }
    }
}
