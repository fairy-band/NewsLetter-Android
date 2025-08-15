package com.fairyband.soak.presentation.feature.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R

@Composable
internal fun SettingScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_setting_leading),
            contentDescription = "back button",
            modifier = Modifier.padding(start = 6.dp)
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.setting_info_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(title = stringResource(R.string.setting_info_user))
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(title = stringResource(R.string.setting_info_alarm))
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = SoakTheme.colors.borderPrimary,
                modifier = Modifier.padding(horizontal = 0.5.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.setting_version_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingText(
                title = stringResource(R.string.setting_version_current),
                subText = "1.0.1.329"
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = SoakTheme.colors.borderPrimary,
                modifier = Modifier.padding(horizontal = 0.5.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.setting_support_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingText(
                title = stringResource(R.string.setting_support_contact),
                subText = stringResource(R.string.setting_support_email)
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = SoakTheme.colors.borderPrimary,
                modifier = Modifier.padding(horizontal = 0.5.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.setting_policy_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(title = stringResource(R.string.setting_policy_service))
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(title = stringResource(R.string.setting_policy_personal))
        }
    }
}

@Composable
private fun SettingArrow(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = SoakTheme.typography.body18.copy(fontWeight = FontWeight.Medium)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_setting_arrow),
            contentDescription = "arrow button"
        )
    }
}

@Composable
private fun SettingText(
    title: String,
    subText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = SoakTheme.typography.body18.copy(fontWeight = FontWeight.Medium)
        )
        Text(
            text = subText,
            style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Normal)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    SoakTheme {
        SettingScreen()
    }
}