package com.nexters.knownknowns.core.designsystem.bottomsheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.nexters.knownknowns.core.theme.KnownKnownsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    dragHandle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = KnownKnownsTheme.colors.backgroundSurface,
        dragHandle = dragHandle,
        shape = RoundedCornerShape(12.dp)
    ) {
        content()
    }
}
