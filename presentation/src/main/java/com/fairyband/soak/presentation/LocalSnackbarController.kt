package com.fairyband.soak.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.theme.SoakTheme

val LocalSnackbarController = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState가 주입되지 않았습니다.")
}

@Composable
fun SoakSnackbar(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(color = SoakTheme.colors.fillWhite, shape = CircleShape)
            .padding(vertical = 12.dp)
            .padding(start = 12.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.ic_check),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Text(
            text = message,
            style = SoakTheme.typography.body15.copy(
                color = SoakTheme.colors.textPrimary,
            ),
        )
    }
}
