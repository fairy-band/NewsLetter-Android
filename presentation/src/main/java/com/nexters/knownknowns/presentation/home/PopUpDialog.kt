package com.nexters.knownknowns.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.R

private data class CarouselItem(
    val id: Int,
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
    val carouselItems = remember {
        listOf(
            CarouselItem(
                id = 0,
                title = "cupcake",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 1,
                title = "donut",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 2,
                title = "eclair",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 3,
                title = "froyo",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 4,
                title = "gingerbread",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
            CarouselItem(
                id = 5,
                title = "plus",
                category = "Kotlin",
                newsLetter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                titleColor = Color(0xFF27C434)
            ),
        )
    }
    val pagerState = rememberPagerState(pageCount = {
        carouselItems.size
    })

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = 20.dp
    val pageSize = screenWidth - (horizontalPadding * 2)

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
                pageSize = PageSize.Fixed(pageSize),
                pageSpacing = 12.dp,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
            ) { pageIndex ->
                val item = carouselItems[pageIndex]
                PopUpItem(
                    title = item.title,
                    category = item.category,
                    newsLetter = item.newsLetter,
                    titleColor = item.titleColor,
                    summary = item.summary,
                    onClick = onDismissRequest,
                )
            }
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
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
