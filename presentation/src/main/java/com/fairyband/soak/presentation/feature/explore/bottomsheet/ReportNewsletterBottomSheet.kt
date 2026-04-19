package com.fairyband.soak.presentation.feature.explore.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.bottomsheet.BaseBottomSheet
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference

// TODO: 키보드 올라왔을 때 처리
// TODO: 뒤로가기 눌렀을 때 키보드 먼저 닫히고 이후 바텀시트 닫기
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportNewsletterBottomSheet(
    name: String,
    url: String,
    selectedPreferences: List<Preference>,
    language: String,
    updateName: (String) -> Unit,
    updateUrl: (String) -> Unit,
    updateLanguage: (String) -> Unit,
    updatePreference: (Preference) -> Unit,
    isSubmitEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    .padding(start = 20.dp, end = 8.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.explore_report_bottomsheet_title),
                        color = SoakTheme.colors.textPrimary,
                        style = SoakTheme.typography.head20.copy(fontWeight = FontWeight.Bold),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.explore_report_bottomsheet_subtitle),
                        color = SoakTheme.colors.textSecondary,
                        style = SoakTheme.typography.body14,
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                        .clickable(onClick = onDismissRequest),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .background(color = SoakTheme.colors.fillSecondary)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.explore_report_close),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // 이름
                FieldLabel(text = stringResource(R.string.explore_report_field_name))
                Spacer(modifier = Modifier.height(8.dp))
                ReportTextField(
                    value = name,
                    onValueChange = updateName,
                    placeholder = stringResource(R.string.explore_report_field_name_hint),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 주소
                FieldLabel(text = stringResource(R.string.explore_report_field_url))
                Spacer(modifier = Modifier.height(8.dp))
                ReportTextField(
                    value = url,
                    onValueChange = updateUrl,
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
                                .clickable { updatePreference(preference) },
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
                    onValueChange = updateLanguage,
                    placeholder = stringResource(R.string.explore_report_field_language_hint),
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            BaseButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                paddingVertical = 16.dp,
                onClick = onSubmit,
                isEnabled = isSubmitEnabled,
                containerColor = SoakTheme.colors.fillPrimaryInverse,
                contentColor = SoakTheme.colors.textStrongInverse,
                shape = RoundedCornerShape(16.dp),
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
        color = SoakTheme.colors.textSecondary,
    )
}

@Composable
private fun ReportTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = SoakTheme.colors.borderPrimary,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        textStyle = SoakTheme.typography.body16.copy(color = SoakTheme.colors.textStrong),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = SoakTheme.typography.body16.copy(color = SoakTheme.colors.textTertiary),
                    )
                }
                innerTextField()
            }
        },
    )
}
