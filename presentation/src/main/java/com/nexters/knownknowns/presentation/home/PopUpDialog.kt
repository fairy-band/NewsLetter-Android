package com.nexters.knownknowns.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private data class CarouselItem(
    val id: Int,
    val title: String,
    val titleColor: Color,
)

@Composable
internal fun PopUpDialog(
    onDismissRequest: () -> Unit,
) {
    val carouselItems = remember {
        listOf(
            CarouselItem(
                id = 0,
                title = "cupcake",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 1,
                title = "donut",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 2,
                title = "eclair",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 3,
                title = "froyo",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 4,
                title = "gingerbread",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 5,
                title = "plus",
                titleColor = Color(0xFF27C434)
            ),
        )
    }
    val pagerState = rememberPagerState(pageCount = {
        carouselItems.size
    })

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val pageSize = 300.dp
    val horizontalPadding = (screenWidth - pageSize) / 2

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismissRequest),
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(300.dp),
                pageSpacing = 12.dp,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
            ) { pageIndex ->
                val item = carouselItems[pageIndex]
                PopUpItem(
                    title = item.title,
                    titleColor = item.titleColor
                )
            }
        }
    }
}
