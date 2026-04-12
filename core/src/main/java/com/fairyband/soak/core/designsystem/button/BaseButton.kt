package com.fairyband.soak.core.designsystem.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.theme.SoakTheme

@Composable
fun BaseButton(
    paddingVertical: Dp,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    borderWidth: Dp = 0.dp,
    borderColor: Color = SoakTheme.colors.borderPrimary,
    containerColor: Color = SoakTheme.colors.backgroundSurface,
    contentColor: Color = SoakTheme.colors.textPrimary,
    disabledContainerColor: Color = SoakTheme.colors.fillDisabled,
    disabledContentColor: Color = SoakTheme.colors.textDisabled,
    content: @Composable () -> Unit,
) {
    Button(
        contentPadding = PaddingValues(vertical = paddingVertical),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .clip(shape = shape),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        onClick = onClick,
    ) {
        content()
    }
}

@Composable
fun BaseButton2(
    label: String,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    paddingVertical: Dp = 14.dp,
    borderWidth: Dp = 0.dp,
    borderColor: Color = SoakTheme.colors.borderPrimary,
    containerColor: Color = SoakTheme.colors.backgroundSurface,
    contentColor: Color = SoakTheme.colors.textPrimary,
    disabledContainerColor: Color = Color.Gray,
    disabledContentColor: Color = Color.White,
) {
    Button(
        contentPadding = PaddingValues(vertical = paddingVertical),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape,
            )
            .clip(shape = shape),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        onClick = onClick,
    ) {
        Text(text = label)
    }
}

@Preview(showBackground = true, name = "BaseButton2 Enabled")
@Composable
private fun BaseButton2EnabledPreview() {
    SoakTheme {
        BaseButton2(
            label = "확인",
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            enabled = true,
        )
    }
}

@Preview(showBackground = true, name = "BaseButton2 Disabled")
@Composable
private fun BaseButton2DisabledPreview() {
    SoakTheme {
        BaseButton2(
            label = "확인",
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            enabled = false,
        )
    }
}
