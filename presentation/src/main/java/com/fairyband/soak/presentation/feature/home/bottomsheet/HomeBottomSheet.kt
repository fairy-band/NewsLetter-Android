package com.fairyband.soak.presentation.feature.home.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.bottomsheet.BaseBottomSheet
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.analytics.SoakAnalytics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeBottomSheet(
    onDismissRequest: () -> Unit,
    onButtonClick: (List<String>, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedPreferences = remember { mutableStateListOf<Preference>() }
    var selectedWorkingExperience by remember { mutableStateOf<WorkingExperience?>(null) }
    val isButtonEnabled = selectedWorkingExperience != null && selectedPreferences.isNotEmpty()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        SoakAnalytics.logBottomSheetCustomPageview()
    }

    BaseBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = { DragHandle() }
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.home_bottomsheet_title),
                color = SoakTheme.colors.textStrong,
                style = SoakTheme.typography.head22.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.home_bottomsheet_position),
                color = SoakTheme.colors.textSecondary,
                style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            PositionList(
                selectedPreferences = selectedPreferences,
                onClick = { position ->
                    if (position in selectedPreferences) selectedPreferences.remove(position)
                    else selectedPreferences.add(position)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.home_bottomsheet_career),
                color = SoakTheme.colors.textSecondary,
                style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            CareerList(
                selectedWorkingExperience = selectedWorkingExperience,
                onClick = { career ->
                    selectedWorkingExperience = career
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            BaseButton(
                paddingVertical = 16.dp,
                onClick = {
                    onDismissRequest()
                    onButtonClick(
                        selectedPreferences.map { it.stringValue },
                        selectedWorkingExperience?.stringValue.orEmpty(),
                    )
                },
                containerColor = SoakTheme.colors.fillPrimaryInverse,
                contentColor = SoakTheme.colors.textStrongInverse,
                shape = CircleShape,
                isEnabled = isButtonEnabled
            ) {
                Text(
                    text = stringResource(id = R.string.home_bottomsheet_button),
                    style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(modifier = Modifier.height(37.dp))
        }
    }
}

@Composable
private fun PositionList(
    selectedPreferences: List<Preference>,
    onClick: (Preference) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Preference.entries.forEach { position ->
            val isSelected = position in selectedPreferences

            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (isSelected) SoakTheme.colors.borderStrong else SoakTheme.colors.borderPrimary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(start = 10.dp, end = 12.dp)
                    .padding(vertical = 8.dp)
                    .noRippleClickable { onClick(position) },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(position.icon),
                    contentDescription = "position image"
                )
                Text(
                    text = position.label,
                    style = SoakTheme.typography.body15.copy(fontWeight = FontWeight.Medium),
                    color = SoakTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun CareerList(
    selectedWorkingExperience: WorkingExperience?,
    onClick: (WorkingExperience) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WorkingExperience.entries.forEach { career ->
            val isSelected = career == selectedWorkingExperience

            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (isSelected) SoakTheme.colors.borderStrong else SoakTheme.colors.borderPrimary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(start = 10.dp, end = 12.dp)
                    .padding(vertical = 8.dp)
                    .noRippleClickable { onClick(career) },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = career.label,
                    style = SoakTheme.typography.body15.copy(fontWeight = FontWeight.Medium),
                    color = SoakTheme.colors.textPrimary
                )
            }
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
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(Modifier.size(width = 40.dp, height = 4.dp))
    }
}
