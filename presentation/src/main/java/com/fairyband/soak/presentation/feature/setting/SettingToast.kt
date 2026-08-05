package com.fairyband.soak.presentation.feature.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R

/**
 * 설정 화면은 TabScreen의 형제 destination이라 LocalSnackbarController를 쓸 수 없어요.
 * 그래서 화면 하단에 직접 띄우는 토스트를 둬요.
 */
@Composable
internal fun SettingToast(
    message: String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = shape)
            .background(color = SoakTheme.colors.fillPrimaryInverse, shape = shape)
            .padding(start = 12.dp, end = 16.dp)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            contentDescription = null,
        )
        Text(
            text = message,
            style = SoakTheme.typography.body15,
            color = SoakTheme.colors.textStrongInverse,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingToastPreview() {
    SoakTheme {
        SettingToast(
            message = stringResource(id = R.string.setting_user_info_change_complete),
        )
    }
}
