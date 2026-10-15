package com.fairyband.soak.presentation.feature

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalSnackbarController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.analytics.SoakAnalytics
import com.fairyband.soak.presentation.feature.explore.ExploreScreen
import com.fairyband.soak.presentation.feature.home.HomeScreen
import com.fairyband.soak.presentation.navigation.TabDestination

private val tabs = listOf(TabDestination.Main, TabDestination.Explore, TabDestination.Bookmark)

@Composable
fun TabScreen() {
    val soakColors = LocalSoakColors.current

    val snackbarHostState = remember { SnackbarHostState() }
    val saveableStateHolder = rememberSaveableStateHolder()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedTab = tabs[selectedIndex]
    val isMain = selectedTab == TabDestination.Main

    val backgroundColor = if (isMain) soakColors.fillSecondary else soakColors.backgroundSurfaceInverse

    // 바텀 내비게이션 관례: 뒤로가기 → 추천 탭 → 앱 종료
    BackHandler(enabled = !isMain) {
        selectedIndex = tabs.indexOf(TabDestination.Main)
    }

    LogTabPageView(selectedTab = selectedTab)

    CompositionLocalProvider(LocalSnackbarController provides snackbarHostState) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .consumeWindowInsets(WindowInsets.navigationBars)
                ) {
                    // 탭마다 rememberSaveable 상태를 따로 보관해 탭을 오가도 스크롤·필터가 유지돼요.
                    // ViewModel은 TabScreen의 ViewModelStore에 묶여 있어 탭 전환 시에도 유지돼요.
                    saveableStateHolder.SaveableStateProvider(key = selectedTab.name) {
                        TabContent(tab = selectedTab)
                    }
                }

                SoakBottomNavigation(
                    selectedTab = selectedTab,
                    isLight = isMain,
                    onTabClick = { tab ->
                        selectedIndex = tabs.indexOf(tab)
                    },
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 56.dp),
                snackbar = { snackbarData ->
                    Row(
                        modifier = Modifier
                            .shadow(elevation = 4.dp, shape = CircleShape)
                            .background(color = SoakTheme.colors.fillWhite, shape = CircleShape)
                            .padding(vertical = 12.dp)
                            .padding(start = 12.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = snackbarData.visuals.message,
                            style = SoakTheme.typography.body15.copy(
                                color = SoakTheme.colors.textPrimary,
                            ),
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun TabContent(tab: TabDestination) {
    when (tab) {
        TabDestination.Main -> HomeScreen()
        TabDestination.Explore -> ExploreScreen()
        TabDestination.Bookmark -> {
            // TODO: 보관함 화면으로 교체
            DarkSystemBar()
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun LogTabPageView(selectedTab: TabDestination) {
    var previousScreenName by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedTab) {
        if (previousScreenName == selectedTab.name) return@LaunchedEffect
        SoakAnalytics.logPageView(
            screenName = selectedTab.name,
            previousScreenName = previousScreenName ?: "none",
            arguments = selectedTab.toString(),
        )
        previousScreenName = selectedTab.name
    }
}

@Composable
private fun SoakBottomNavigation(
    selectedTab: TabDestination,
    isLight: Boolean,
    onTabClick: (TabDestination) -> Unit,
) {
    val soakColors = SoakTheme.colors
    val backgroundColor = if (isLight) soakColors.fillWhite else soakColors.fillPrimaryInverse
    val borderColor = if (isLight) soakColors.borderActive else soakColors.borderPrimaryInverse
    val selectedColor = if (isLight) soakColors.textStrong else soakColors.textStrongInverse

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == selectedTab
                BottomNavigationItem(
                    modifier = Modifier.weight(1f),
                    iconRes = tab.iconRes(isSelected),
                    labelRes = tab.labelRes(),
                    color = if (isSelected) selectedColor else soakColors.iconTabInactive,
                    isSelected = isSelected,
                    onClick = { if (!isSelected) onTabClick(tab) },
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(top = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = color,
        )
        Text(
            text = stringResource(labelRes),
            style = SoakTheme.typography.caption11.copy(
                color = color,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            ),
        )
    }
}

@DrawableRes
private fun TabDestination.iconRes(isSelected: Boolean): Int = when (this) {
    TabDestination.Main -> R.drawable.ic_tab_recommend
    TabDestination.Explore -> R.drawable.ic_tab_explore
    TabDestination.Bookmark -> if (isSelected) R.drawable.ic_tab_bookmark_selected else R.drawable.ic_tab_bookmark
}

@StringRes
private fun TabDestination.labelRes(): Int = when (this) {
    TabDestination.Main -> R.string.tab_recommend
    TabDestination.Explore -> R.string.tab_explore
    TabDestination.Bookmark -> R.string.tab_bookmark
}

@Preview
@Composable
private fun SoakBottomNavigationLightPreview() {
    SoakTheme {
        SoakBottomNavigation(selectedTab = TabDestination.Main, isLight = true, onTabClick = {})
    }
}

@Preview
@Composable
private fun SoakBottomNavigationDarkPreview() {
    SoakTheme {
        SoakBottomNavigation(selectedTab = TabDestination.Bookmark, isLight = false, onTabClick = {})
    }
}
