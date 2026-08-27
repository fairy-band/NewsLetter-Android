package com.fairyband.soak.presentation.feature.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.navigation.MainDestination
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarkdownDetailScreen(
    detail: MainDestination.MarkdownDetail,
    viewModel: MarkdownDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    // 마크다운을 못 받으면 원문 웹뷰로 대체해요. replace 라서 뒤로가기 시 이 화면을 거치지 않아요.
    LaunchedEffect(state) {
        if (state is MarkdownDetailState.Fallback) {
            navController.replace(MainDestination.WebView(url = detail.fallbackUrl))
        }
    }

    MarkdownDetailScreen(
        state = state,
        title = detail.title,
        pointColor = Color(detail.pointColorArgb),
        onBackClick = navController::pop,
    )
}

@Composable
fun MarkdownDetailScreen(
    state: MarkdownDetailState,
    title: String,
    pointColor: Color,
    onBackClick: () -> Unit,
) {
    DarkSystemBar()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MarkdownColors.background)
            .statusBarsPadding(),
    ) {
        MarkdownDetailTopBar(
            title = title,
            onBackClick = onBackClick,
        )

        when (state) {
            MarkdownDetailState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = pointColor)
            }

            is MarkdownDetailState.Success -> MarkdownView(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(20.dp),
                markdown = state.markdown,
                pointColor = pointColor,
            )

            // 웹뷰로 넘어가는 중이라 별도 화면을 그리지 않아요.
            MarkdownDetailState.Fallback -> Unit
        }
    }
}

@Composable
private fun MarkdownDetailTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = MarkdownColors.textStrong,
                )
            }

            Text(
                text = title,
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold),
                color = MarkdownColors.textStrong,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        HorizontalDivider(color = MarkdownColors.divider)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F)
@Composable
private fun MarkdownDetailScreenPreview() {
    SoakTheme {
        MarkdownDetailScreen(
            state = MarkdownDetailState.Success(
                markdown = """
                    # 메타의 초지능 AI 연구소 동향

                    ## 요약

                    메타는 **초지능 연구소**를 새로 꾸리고 `Scale AI` 인력을 흡수했다.

                    - **조직 개편** — 전체 AI 부문 명칭 변경
                    - **핵심 전담팀** — 신규 영입 연구원 중심

                    > [!tip]
                    > 오픈소스 정책은 아직 확정되지 않았어요.
                """.trimIndent(),
            ),
            title = "GeekNews",
            pointColor = Color(0xFF68A4E7),
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F)
@Composable
private fun MarkdownDetailScreenLoadingPreview() {
    SoakTheme {
        MarkdownDetailScreen(
            state = MarkdownDetailState.Loading,
            title = "GeekNews",
            pointColor = Color(0xFF68A4E7),
            onBackClick = {},
        )
    }
}
