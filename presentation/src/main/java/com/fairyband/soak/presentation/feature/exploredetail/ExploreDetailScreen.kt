package com.fairyband.soak.presentation.feature.exploredetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.designsystem.systembar.DarkSystemBar
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.model.ExploreFeed
import org.koin.androidx.compose.koinViewModel

// TODO: nav3에서 넘겨 받은 상태를 ViewModel에서 바로 관리하는 방법 찾아보기 (like savedStateHandle)
@Composable
fun ExploreDetailScreen(
    feeds: List<ExploreFeed>,
    index: Int,
    viewModel: ExploreDetailViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val selectedIndex by viewModel.selectedIndex.collectAsStateWithLifecycle()

    // TODO: feeds 안정성 확인하기
    LaunchedEffect(index, feeds) {
        viewModel.initialize(feeds = feeds, index = index)
    }

    DetailBackground()

    Row(
        modifier = Modifier
            .systemBarsPadding()
            .height(48.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
    ) {
        IconButton(
            onClick = {
                navController.pop()
            },
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = SoakTheme.colors.iconStrongInverse,
            )
        }
    }
}

@Composable
private fun DetailBackground() {
    DarkSystemBar()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.home_drawer_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 84.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .blur(20.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SoakTheme.colors.backgroundSurfaceInverse,
                            Color.Transparent
                        )
                    )
                )

        )
    }
}