package com.fairyband.soak.presentation.feature.setting.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R

@Composable
internal fun SettingPersonalScreen(
    paddingValues: PaddingValues,
) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Image(
            imageVector = ImageVector.Companion.vectorResource(id = R.drawable.ic_setting_leading),
            contentDescription = "back button",
            modifier = Modifier.Companion
                .padding(start = 6.dp)
                .noRippleClickable(navController::pop)
        )
        Column(
            modifier = Modifier.Companion
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.setting_personal),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Companion.Normal),
                color = SoakTheme.colors.textSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingPersonalScreenPreview() {
    SoakTheme {
        SettingPersonalScreen(
            paddingValues = PaddingValues()
        )
    }
}