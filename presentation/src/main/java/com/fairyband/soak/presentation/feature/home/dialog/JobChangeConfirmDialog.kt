package com.fairyband.soak.presentation.feature.home.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.designsystem.dialog.BaseDialog
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R

@Composable
internal fun JobChangeConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseDialog(
        onDismiss = onDismissRequest,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        containerColor = SoakTheme.colors.backgroundSurface,
        horizontalMargin = 30.dp,
        contentPadding = PaddingValues(top = 26.dp, bottom = 16.dp, start = 18.dp, end = 18.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = SoakTheme.colors.textSecondary)) {
                    append(stringResource(id = R.string.home_bottomsheet_job_change_dialog_message_prefix))
                }
                withStyle(
                    SpanStyle(
                        color = SoakTheme.colors.textStrong,
                        fontWeight = FontWeight.SemiBold,
                    )
                ) {
                    append(stringResource(id = R.string.home_bottomsheet_job_change_dialog_message_highlight))
                }
                withStyle(SpanStyle(color = SoakTheme.colors.textSecondary)) {
                    append(stringResource(id = R.string.home_bottomsheet_job_change_dialog_message_suffix))
                }
            },
            style = SoakTheme.typography.body16,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(26.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DialogButton(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.home_bottomsheet_job_change_dialog_cancel),
                onClick = onDismissRequest,
                containerColor = SoakTheme.colors.fillPrimary,
                contentColor = SoakTheme.colors.textTertiary,
            )
            DialogButton(
                modifier = Modifier.weight(1f),
                label = stringResource(id = R.string.home_bottomsheet_job_change_dialog_confirm),
                onClick = onConfirm,
                containerColor = SoakTheme.colors.fillPrimaryInverse,
                contentColor = SoakTheme.colors.textStrongInverse,
            )
        }
    }
}

@Composable
private fun DialogButton(
    label: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    BaseButton(
        modifier = modifier.height(50.dp),
        paddingVertical = 0.dp,
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = label,
            style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JobChangeConfirmDialogPreview() {
    SoakTheme {
        JobChangeConfirmDialog(
            onDismissRequest = {},
            onConfirm = {},
        )
    }
}
