package com.nexters.knownknowns.presentation.home

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
import com.nexters.knownknowns.presentation.home.PopUpItemDefaults.MAX_LINE

@Composable
internal fun PopUpItem(
    title: String,
    category: String,
    newsLetter: String,
    titleColor: Color,
    summary: String,
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
            text = title,
            style = KnownKnownsTheme.typography.head20.copy(fontWeight = FontWeight.Bold),
            color = titleColor
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = category,
                style = KnownKnownsTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                color = titleColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            VerticalDivider(
                thickness = 1.dp,
                color = titleColor,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = newsLetter,
                style = KnownKnownsTheme.typography.body13.copy(fontWeight = FontWeight.Medium),
                color = titleColor
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = summary,
            style = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.Normal),
            color = KnownKnownsTheme.colors.textPrimary,
            maxLines = MAX_LINE,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        BaseButton(
            paddingVertical = 12.dp,
            onClick = onClick,
            backgroundColor = KnownKnownsTheme.colors.backgroundSurface,
            textColor = KnownKnownsTheme.colors.textPrimary,
            cornerRadius = 16.dp,
            text = stringResource(id = R.string.home_popup_button_text),
            textStyle = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

private object PopUpItemDefaults {
    const val MAX_LINE = 8
}

@Preview(showBackground = true)
@Composable
private fun PopUpItemPreview() {
    KnownKnownsTheme {
        PopUpItem(
            title = "IT 직장인이라면 알아야 할 주 4일제의 모든 것을 알려준다",
            category = "Kotlin",
            newsLetter = "안드로이드 위클리",
            titleColor = KnownKnownsTheme.colors.statePositivePrimary,
            summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
            onClick = {},
        )
    }
}
