package com.nexters.knownknowns.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.knownknowns.core.theme.KnownKnownsTheme
import com.nexters.knownknowns.presentation.home.PopUpItemDefaults.POP_UP_HEIGHT

@Composable
internal fun PopUpItem(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = KnownKnownsTheme.colors.fillPrimaryInverse,
                shape = RoundedCornerShape(16.dp),
            )
            .fillMaxWidth()
            .height(POP_UP_HEIGHT),
    ) {

    }
}

private object PopUpItemDefaults {
    val POP_UP_HEIGHT = 366.dp
}

@Preview(showBackground = true)
@Composable
private fun PopUpItemPreview() {
    KnownKnownsTheme {
        PopUpItem()
    }
}
