package com.fairyband.soak.presentation.feature.explore

import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.theme.LocalSoakColors
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.data.model.response.ExploreContentResponse
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.dialog.Language
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.CARD_HEIGHT
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.SUMMARY_MAX_LINE
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.TITLE_MAX_LINE
import com.fairyband.soak.presentation.model.NewsFeed
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
    var showDialog by remember { mutableStateOf(false) }

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
                Card(
                    modifier = Modifier.clickable {
                        showDialog = true
                    },
                    content = news[index],
                    containerColor = cardColors[index % 6],
                )
            }
        }
    }

    if (showDialog) {
        PopUpItem(
            newsFeed = NewsFeed(
                id = 1,
                title = "SwiftUI 커스텀 뷰, 유리처럼 만드는 비법!",
                keyword = "SwiftUI",
                letter = "DZon Newsletters",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\n" +
                        "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                url = "",
                language = "",
            ),
            onWebClick = {},
            titleColor = SoakTheme.colors.statePositivePrimary,
        )
    }
}

@Composable
private fun Card(
    content: ExploreContentResponse,
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

@Composable
private fun PopUpItem(
    newsFeed: NewsFeed,
    titleColor: Color,
    onWebClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = modifier
                .background(
                    color = SoakTheme.colors.backgroundBase,
                    shape = RoundedCornerShape(16.dp),
                )
                .fillMaxWidth()
                .height(CARD_HEIGHT)
                .padding(24.dp),
        ) {
            var titleLineCount by remember { mutableIntStateOf(1) }

            Text(
                text = newsFeed.title,
                style = SoakTheme.typography.head20.copy(fontWeight = FontWeight.Bold),
                color = titleColor,
                maxLines = TITLE_MAX_LINE,
                onTextLayout = { textLayout ->
                    titleLineCount = textLayout.lineCount
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = newsFeed.keyword,
                    style = SoakTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                    color = titleColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                VerticalDivider(color = titleColor)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = newsFeed.letter,
                    style = SoakTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                    color = titleColor
                )
            }
            Spacer(modifier = Modifier.height(if (titleLineCount == TITLE_MAX_LINE) 16.dp else 44.dp))
            Text(
                text = newsFeed.summary,
                style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.Normal),
                color = SoakTheme.colors.textPrimary,
                maxLines = SUMMARY_MAX_LINE,
                minLines = SUMMARY_MAX_LINE,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            BaseButton(
                paddingVertical = 12.dp,
                onClick = onWebClick,
                shape = CircleShape,
                borderWidth = 1.dp,
                borderColor = SoakTheme.colors.borderSecondary
            ) {
                Text(
                    text = stringResource(id = R.string.home_popup_button_text),
                    style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}