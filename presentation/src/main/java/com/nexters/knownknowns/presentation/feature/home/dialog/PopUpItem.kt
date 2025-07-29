package com.nexters.knownknowns.presentation.feature.home.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.knownknowns.core.designsystem.button.BaseButton
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.R
import com.nexters.knownknowns.presentation.feature.home.dialog.PopUpDialogDefaults.SUMMARY_MAX_LINE
import com.nexters.knownknowns.presentation.feature.home.dialog.PopUpDialogDefaults.TITLE_MAX_LINE
import com.nexters.knownknowns.presentation.model.NewsFeed

@Composable
internal fun PopUpItem(
    newsFeed: NewsFeed,
    titleColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = KnownKnownsTheme.colors.backgroundBase,
                shape = RoundedCornerShape(16.dp),
            )
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        Text(
            text = newsFeed.title,
            style = KnownKnownsTheme.typography.head20.copy(fontWeight = FontWeight.Bold),
            color = titleColor,
            minLines = TITLE_MAX_LINE,
            maxLines = TITLE_MAX_LINE
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = newsFeed.keyword,
                style = KnownKnownsTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                color = titleColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            VerticalDivider(
                color = titleColor,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = newsFeed.letter,
                style = KnownKnownsTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                color = titleColor
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = newsFeed.summary,
            style = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.Normal),
            color = KnownKnownsTheme.colors.textPrimary,
            maxLines = SUMMARY_MAX_LINE,
            minLines = SUMMARY_MAX_LINE,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        BaseButton(
            paddingVertical = 12.dp,
            onClick = onClick,
            containerColor = KnownKnownsTheme.colors.backgroundSurface,
            contentColor = KnownKnownsTheme.colors.textPrimary,
            shape = CircleShape,
            text = stringResource(id = R.string.home_popup_button_text),
            textStyle = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold),
            borderWidth = 1.dp,
            borderColor = KnownKnownsTheme.colors.borderSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PopUpItemPreview() {
    KnownKnownsTheme {
        PopUpItem(
            newsFeed = NewsFeed(
                id = "1",
                title = "IT 직장인이라면 알아야 할 주 4일제의 모든 것을 알려준다",
                keyword = "Kotlin",
                letter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                url = ""
            ),
            titleColor = KnownKnownsTheme.colors.statePositivePrimary,
            onClick = {},
        )
    }
}
