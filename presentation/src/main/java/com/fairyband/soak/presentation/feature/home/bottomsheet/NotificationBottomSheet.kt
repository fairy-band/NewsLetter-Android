package com.fairyband.soak.presentation.feature.home.bottomsheet

import android.Manifest
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.bottomsheet.BaseBottomSheet
import com.fairyband.soak.core.designsystem.button.BaseButton
import com.fairyband.soak.core.extension.openAppNotificationSettings
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.analytics.SoakAnalytics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return@rememberLauncherForActivityResult

        val shouldOpenSetting =
            activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == false

        if (shouldOpenSetting) {
            context.openAppNotificationSettings()
        }
    }

    LaunchedEffect (Unit) {
        SoakAnalytics.logBottomSheetNotificationPageview()
    }

    BaseBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismissRequest,
        dragHandle = { DragHandle() }
    ) {
        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.home_notification_setting_title),
                color = SoakTheme.colors.textStrong,
                style = SoakTheme.typography.head22.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.home_notification_setting_description),
                color = SoakTheme.colors.textTertiary,
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.height(40.dp))
            BaseButton(
                paddingVertical = 16.dp,
                onClick = {
                    onDismissRequest()
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    SoakAnalytics.logBottomSheetNotificationClick()
                },
                containerColor = SoakTheme.colors.fillPrimaryInverse,
                contentColor = SoakTheme.colors.textStrongInverse,
                shape = CircleShape,
            ) {
                Text(
                    text = stringResource(id = R.string.home_notification_setting_button),
                    style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DragHandle(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(top = 12.dp, bottom = 8.dp),
        color = SoakTheme.colors.fillPrimary,
        shape = CircleShape
    ) {
        Box(Modifier.size(width = 40.dp, height = 4.dp))
    }
}
