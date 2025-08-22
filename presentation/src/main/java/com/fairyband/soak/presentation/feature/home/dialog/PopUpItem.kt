package com.fairyband.soak.presentation.feature.home.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.SUMMARY_MAX_LINE
import com.fairyband.soak.presentation.feature.home.dialog.PopUpDialogDefaults.TITLE_MAX_LINE
import com.fairyband.soak.presentation.model.NewsFeed

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
                color = SoakTheme.colors.backgroundBase,
                shape = RoundedCornerShape(16.dp),
            )
            .fillMaxWidth()
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
        ) {
            Text(
                text = newsFeed.keyword,
                style = SoakTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
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
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BaseButton(
                modifier = Modifier.size(44.dp),
                paddingVertical = 12.dp,
                onClick = onClick,
                shape = CircleShape,
                borderWidth = 1.dp,
                borderColor = SoakTheme.colors.borderSecondary
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_share),
                    contentDescription = "share image",
                )
            }
            BaseButton(
                paddingVertical = 12.dp,
                onClick = onClick,
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

@Preview(showBackground = true)
@Composable
private fun PopUpItemPreview() {
    SoakTheme {
        PopUpItem(
            newsFeed = NewsFeed(
                id = "1",
                title = "IT 직장인이라면 알아야 할 주 4일제의 모든 것을 알려준다",
                keyword = "Kotlin",
                letter = "안드로이드 위클리",
                summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
                url = ""
            ),
            titleColor = SoakTheme.colors.statePositivePrimary,
            onClick = {},
        )
    }
}
