package com.fairyband.soak.presentation.feature.explore

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.data.model.request.Direction
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.LocalSnackbarController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.analytics.SoakAnalytics
import com.fairyband.soak.presentation.feature.explore.bottomsheet.ReportNewsletterBottomSheet
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
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
    val directionOrder = state.direction

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    val reportSuccessMessage = stringResource(R.string.explore_report_success)

    LaunchedEffect(directionOrder, state.selectedJobFilters) {
        lazyState.scrollToItem(0)
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
        Column {
            JobFilterRow(
                selectedJobFilters = state.selectedJobFilters,
                isAllSelected = state.isAllJobFiltersSelected,
                onAllClick = viewModel::selectAllJobFilters,
                onJobClick = viewModel::toggleJobFilter,
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = stringResource(R.string.explore_count_of_articles, totalCount),
                        style = SoakTheme.typography.body14.copy(
                            color = soakColors.textStrongInverse,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Row(
                        modifier = Modifier.clickable { viewModel.toggleOrder() },
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_order),
                            contentDescription = null,
                            tint = Color.White,
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = stringResource(
                                if (directionOrder == Direction.DESC) R.string.explore_newest
                                else R.string.explore_oldest
                            ),
                            style = SoakTheme.typography.body13.copy(
                                color = soakColors.textStrongInverse,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                LazyVerticalGrid(
                    state = lazyState,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                ) {
                    items(count = feeds.size, key = { index -> feeds[index].id }) { index ->
                        Card(
                            modifier = Modifier.clickable {
                                navController.navigate(
                                    MainDestination.ExploreDetail(
                                        feeds = feeds,
                                        index = index,
                                        totalCount = totalCount,
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
                SoakAnalytics.logExploreReportSubmitClick(
                    name = state.name,
                    url = state.url,
                    jobGroup = state.selectedPreferences.joinToString(",") { it.stringValue },
                    language = state.language,
                )
                viewModel.reportNewsletter()
            },
        )
    }
}

@Composable
private fun JobFilterRow(
    selectedJobFilters: List<Preference>,
    isAllSelected: Boolean,
    onAllClick: () -> Unit,
    onJobClick: (Preference) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item {
            JobFilterChip(
                label = stringResource(R.string.explore_filter_all),
                isSelected = isAllSelected,
                onClick = onAllClick,
            )
        }
        items(items = Preference.entries, key = { it.name }) { preference ->
            JobFilterChip(
                label = preference.label,
                icon = preference.icon,
                isSelected = preference in selectedJobFilters,
                onClick = { onJobClick(preference) },
            )
        }
    }
}

@Composable
private fun JobFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
) {
    Row(
        modifier = modifier
            .background(
                color = if (isSelected) SoakTheme.colors.backgroundOnSurface else Color.Transparent,
                shape = CircleShape,
            )
            .border(
                width = if (isSelected) 1.dp else 0.5.dp,
                color = if (isSelected) SoakTheme.colors.borderPrimary else SoakTheme.colors.borderActive,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .noRippleClickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(icon),
                contentDescription = null,
            )
        }
        Text(
            text = label,
            style = SoakTheme.typography.caption12.copy(
                color = if (isSelected) SoakTheme.colors.textPrimary else SoakTheme.colors.textStrongInverse,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            ),
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
