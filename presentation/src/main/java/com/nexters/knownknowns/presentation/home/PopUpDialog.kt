package com.nexters.knownknowns.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.R

private data class CarouselItem(
    val title: String,
    val category: String,
    val newsLetter: String,
    val titleColor: Color,
    val summary: String,
)

@Composable
internal fun PopUpDialog(
    onDismissRequest: () -> Unit,
) {
    val dummyItems = remember {
        listOf(
            CarouselItem(
                title = "cupcake",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                title = "donut",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                title = "eclair",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                title = "froyo",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                title = "gingerbread",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                title = "plus",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
        )
    }
    val pagerState = rememberPagerState(pageCount = {
        dummyItems.size
    })

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val pageSize = screenWidth * 0.8f
    val horizontalPadding = (screenWidth - pageSize) / 2

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismissRequest),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(pageSize),
                pageSpacing = 12.dp,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                key = { it }
            ) { pageIndex ->
                val item = dummyItems[pageIndex]
                PopUpItem(
                    title = item.title,
                    category = item.category,
                    newsLetter = item.newsLetter,
                    titleColor = item.titleColor,
                    summary = item.summary,
                    onClick = onDismissRequest,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Indicator(
                pageCount = dummyItems.size,
                pageIndex = pagerState.currentPage
            )
            Spacer(modifier = Modifier.height(42.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_popup_dismiss),
                contentDescription = "pop up dismiss button",
                modifier = Modifier
                    .background(
                        color = KnownKnownsTheme.colors.backgroundBase.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(13.dp)
            )
        }
    }
}

@Composable
private fun Indicator(
    pageCount: Int,
    pageIndex: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == pageIndex

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .size(8.dp)
                    .background(
                        if (isSelected) KnownKnownsTheme.colors.backgroundBase
                        else KnownKnownsTheme.colors.backgroundBase.copy(alpha = 0.3f)
                    )
            )
        }
    }
}
