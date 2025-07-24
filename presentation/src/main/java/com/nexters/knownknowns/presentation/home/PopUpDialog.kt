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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private data class CarouselItem(
    val id: Int,
    val contentDescription: String
)

@Composable
internal fun PopUpDialog(
    onDismissRequest: () -> Unit,
) {
    val carouselItems = remember {
        listOf(
            CarouselItem(0, "cupcake"),
            CarouselItem(1, "donut"),
            CarouselItem(2, "eclair"),
            CarouselItem(3, "froyo"),
            CarouselItem(4, "gingerbread"),
            CarouselItem(5, "plus"),
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
                PopUpItem()
            }
        }
    }
}
