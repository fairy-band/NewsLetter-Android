package com.nexters.knownknowns.core.designsystem.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nexters.knownknowns.core.theme.KnownKnownsTheme

@Composable
fun BaseButton(
    paddingVertical: Dp,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    shape: Shape,
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 1.dp,
    borderColor: Color = KnownKnownsTheme.colors.borderPrimary
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
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
        ),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = textStyle,
        )
    }
}
