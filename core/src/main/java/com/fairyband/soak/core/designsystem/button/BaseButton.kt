package com.fairyband.soak.core.designsystem.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.theme.KnownKnownsTheme

@Composable
fun BaseButton(
    paddingVertical: Dp,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    borderWidth: Dp = 0.dp,
    borderColor: Color = KnownKnownsTheme.colors.borderPrimary,
    containerColor: Color = KnownKnownsTheme.colors.backgroundSurface,
    contentColor: Color = KnownKnownsTheme.colors.textPrimary,
    disabledContainerColor: Color = KnownKnownsTheme.colors.fillDisabled,
    disabledContentColor: Color = KnownKnownsTheme.colors.textDisabled,
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
            ),
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
