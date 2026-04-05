package com.fairyband.soak.presentation.feature.explore.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.bottomsheet.BaseBottomSheet
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportNewsletterBottomSheet(
    onDismissRequest: () -> Unit,
    onSubmit: (name: String, url: String, preferences: List<String>, language: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    val selectedPreferences = remember { mutableStateListOf<Preference>() }
    var language by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BaseBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {},
    ) {
        Column(modifier = modifier) {
            // 헤더: 타이틀 + 닫기 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp, top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.explore_report_bottomsheet_title),
                        color = SoakTheme.colors.textStrong,
                        style = SoakTheme.typography.head22.copy(fontWeight = FontWeight.Bold),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.explore_report_bottomsheet_subtitle),
                        color = SoakTheme.colors.textSecondary,
                        style = SoakTheme.typography.body14,
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .noRippleClickable { onDismissRequest() },
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.ic_explore_close),
                        contentDescription = stringResource(R.string.explore_report_close),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // 이름
                FieldLabel(text = stringResource(R.string.explore_report_field_name))
                Spacer(modifier = Modifier.height(8.dp))
                ReportTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(R.string.explore_report_field_name_hint),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 주소
                FieldLabel(text = stringResource(R.string.explore_report_field_url))
                Spacer(modifier = Modifier.height(8.dp))
                ReportTextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = stringResource(R.string.explore_report_field_url_hint),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 직군
                FieldLabel(text = stringResource(R.string.explore_report_field_job))
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Preference.entries.forEach { preference ->
                        val isSelected = preference in selectedPreferences
                        Row(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) SoakTheme.colors.borderStrong else SoakTheme.colors.borderPrimary,
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .padding(start = 10.dp, end = 12.dp)
                                .padding(vertical = 8.dp)
                                .noRippleClickable {
                                    if (preference in selectedPreferences) selectedPreferences.remove(preference)
                                    else selectedPreferences.add(preference)
                                },
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(preference.icon),
                                contentDescription = preference.label,
                            )
                            Text(
                                text = preference.label,
                                style = SoakTheme.typography.body15.copy(fontWeight = FontWeight.Medium),
                                color = SoakTheme.colors.textPrimary,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 언어
                FieldLabel(text = stringResource(R.string.explore_report_field_language))
                Spacer(modifier = Modifier.height(8.dp))
                ReportTextField(
                    value = language,
                    onValueChange = { language = it },
                    placeholder = stringResource(R.string.explore_report_field_language_hint),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BaseButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                paddingVertical = 16.dp,
                onClick = {
                    onSubmit(
                        name,
                        url,
                        selectedPreferences.map { it.stringValue },
                        language,
                    )
                },
                containerColor = SoakTheme.colors.fillPrimaryInverse,
                contentColor = SoakTheme.colors.textStrongInverse,
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.explore_report_bottomsheet_button),
                    style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold),
                )
            }

            Spacer(
                modifier = Modifier.height(
                    16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            )
        }
    }
}

@Composable
private fun FieldLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = SoakTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold),
        color = SoakTheme.colors.textPrimary,
    )
}

@Composable
private fun ReportTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = SoakTheme.typography.body15.copy(color = SoakTheme.colors.textTertiary),
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SoakTheme.colors.borderStrong,
            unfocusedBorderColor = SoakTheme.colors.borderPrimary,
            focusedTextColor = SoakTheme.colors.textStrong,
            unfocusedTextColor = SoakTheme.colors.textStrong,
            cursorColor = SoakTheme.colors.textStrong,
        ),
        singleLine = true,
    )
}
