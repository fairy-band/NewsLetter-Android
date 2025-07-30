package com.nexters.knownknowns.presentation.feature.home.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BaseBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
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
            Spacer(modifier = Modifier.height(12.dp))
            PositionList()
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.home_bottomsheet_career),
                color = KnownKnownsTheme.colors.textSecondary,
                style = KnownKnownsTheme.typography.body14.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            CareerList()
            Spacer(modifier = Modifier.height(32.dp))
            BaseButton(
                paddingVertical = 16.dp,
                onClick = onDismissRequest,
                containerColor = KnownKnownsTheme.colors.fillPrimaryInverse,
                contentColor = KnownKnownsTheme.colors.textStrongInverse,
                shape = CircleShape,
                isEnabled = true
            ) {
                Text(
                    text = stringResource(id = R.string.home_bottomsheet_button),
                    style = KnownKnownsTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(modifier = Modifier.height(37.dp))
        }
    }
}

@Composable
private fun PositionList(
    modifier: Modifier = Modifier,
) {
    val selectedPositions = remember { mutableStateListOf<Position>() }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Position.entries.forEach { position ->
            val isSelected = position in selectedPositions

            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (isSelected) KnownKnownsTheme.colors.borderStrong else KnownKnownsTheme.colors.borderPrimary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(start = 10.dp, end = 12.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        if (isSelected) selectedPositions.remove(position)
                        else selectedPositions.add(position)
                    },
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
                    style = KnownKnownsTheme.typography.body15.copy(fontWeight = FontWeight.Medium),
                    color = KnownKnownsTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun CareerList(
    modifier: Modifier = Modifier,
) {
    var selectedCareer by remember { mutableStateOf<Career?>(null) }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Career.entries.forEach { career ->
            val isSelected = career == selectedCareer

            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (isSelected) KnownKnownsTheme.colors.borderStrong else KnownKnownsTheme.colors.borderPrimary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(start = 10.dp, end = 12.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedCareer = career
                    },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = career.label,
                    style = KnownKnownsTheme.typography.body15.copy(fontWeight = FontWeight.Medium),
                    color = KnownKnownsTheme.colors.textPrimary
                )
            }
        }
    }
}
