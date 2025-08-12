package com.fairyband.soak.presentation.feature.home.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.bottomsheet.BaseBottomSheet
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationBottomSheet(
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedPreferences = remember { mutableStateListOf<Preference>() }
    var selectedWorkingExperience by remember { mutableStateOf<WorkingExperience?>(null) }
    val isButtonEnabled = selectedWorkingExperience != null && selectedPreferences.isNotEmpty()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BaseBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = { DragHandle() }
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.home_notification_setting_title),
                color = SoakTheme.colors.textStrong,
                style = SoakTheme.typography.head22.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.home_notification_setting_description),
                color = SoakTheme.colors.textTertiary,
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.height(40.dp))
            BaseButton(
                paddingVertical = 16.dp,
                onClick = {
                    onDismissRequest()
                    onButtonClick()
                },
                containerColor = SoakTheme.colors.fillPrimaryInverse,
                contentColor = SoakTheme.colors.textStrongInverse,
                shape = CircleShape,
                isEnabled = isButtonEnabled
            ) {
                Text(
                    text = stringResource(id = R.string.home_notification_setting_button),
                    style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DragHandle(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(top = 12.dp, bottom = 8.dp),
        color = SoakTheme.colors.fillPrimary,
        shape = CircleShape
    ) {
        Box(Modifier.size(width = 40.dp, height = 4.dp))
    }
}
