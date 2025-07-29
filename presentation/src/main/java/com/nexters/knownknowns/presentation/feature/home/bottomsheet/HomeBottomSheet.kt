package com.nexters.knownknowns.presentation.feature.home.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.knownknowns.core.designsystem.bottomsheet.BaseBottomSheet
import com.nexters.knownknowns.core.designsystem.button.BaseButton
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    BaseBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.home_bottomsheet_title),
                color = KnownKnownsTheme.colors.textStrong,
                style = KnownKnownsTheme.typography.head22.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.home_bottomsheet_position),
                color = KnownKnownsTheme.colors.textSecondary,
                style = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = stringResource(id = R.string.home_bottomsheet_career),
                color = KnownKnownsTheme.colors.textSecondary,
                style = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold)
            )
            BaseButton(
                paddingVertical = 16.dp,
                onClick = {},
                containerColor = KnownKnownsTheme.colors.fillPrimaryInverse,
                contentColor = KnownKnownsTheme.colors.textStrongInverse,
                shape = CircleShape,
                text = stringResource(id = R.string.home_bottomsheet_button),
                textStyle = KnownKnownsTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold),
                isEnabled = true
            )
            Spacer(modifier = Modifier.height(37.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBottomSheetPreview() {
    KnownKnownsTheme {
        HomeBottomSheet(
            onDismissRequest = {}
        )
    }
}
