package com.fairyband.soak.presentation.feature.setting

import android.content.ClipData
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fairyband.soak.core.designsystem.dialog.BaseDialog
import com.fairyband.soak.core.extension.noRippleClickable
import com.fairyband.soak.core.extension.openAppNotificationSettings
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.BuildConfig
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.feature.home.bottomsheet.HomeBottomSheet
import com.fairyband.soak.presentation.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
internal fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    var bottomSheetVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (bottomSheetVisibility) {
        HomeBottomSheet(
            onDismissRequest = {
                bottomSheetVisibility = false
            },
            onButtonClick = { preferences, workingExperience ->
                viewModel.saveUserInfo(
                    preferences = preferences,
                    workingExperience = workingExperience
                )

                buttonClickEvent(jobGroup = preferences, careerLevel = workingExperience)
            }
        )
    }

    SettingScreen(
        onInfoUserClick = {
            bottomSheetVisibility = true
        },
        onNotificationClick = (context::openAppNotificationSettings),
        onBackClick = (navController::pop),
        onServiceClick = {
            navController.navigate(Screen.SettingService)
        },
        onPersonalClick = {
            navController.navigate(Screen.SettingPersonal)
        },
    )
}

@Composable
private fun SettingScreen(
    onInfoUserClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onBackClick: () -> Unit,
    onServiceClick: () -> Unit,
    onPersonalClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var installationToken by rememberSaveable { mutableStateOf("알 수 없음") }
    var versionClickCount by rememberSaveable { mutableStateOf(0) }
    val showInstallationToken by remember { derivedStateOf { versionClickCount >= 10 } }

    LaunchedEffect(Unit) {
        FirebaseInstallations.getInstance().getToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("설치 인증 토큰: ${task.result?.token}")
                    task.result?.token?.let { token ->
                        installationToken = token
                    }
                } else {
                    Timber.w("AB 테스트를 위한 파이어베이스 설치 토큰을 받지 못했어요.")
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_setting_leading),
            contentDescription = "back button",
            modifier = Modifier
                .padding(start = 6.dp)
                .noRippleClickable(onBackClick)
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.setting_info_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(
                title = stringResource(R.string.setting_info_user),
                modifier = Modifier.clickable(onClick = onInfoUserClick)
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(
                title = stringResource(R.string.setting_info_alarm),
                modifier = Modifier.clickable(onClick = onNotificationClick)
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SoakTheme.colors.borderPrimary)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.setting_version_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingText(
                modifier = Modifier.noRippleClickable {
                    versionClickCount++
                },
                title = stringResource(R.string.setting_version_current),
                subText = BuildConfig.VERSION_NAME
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SoakTheme.colors.borderPrimary)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.setting_support_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingText(
                title = stringResource(R.string.setting_support_contact),
                subText = stringResource(R.string.setting_support_email)
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = SoakTheme.colors.borderPrimary)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.setting_policy_title),
                style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Medium),
                color = SoakTheme.colors.textTertiary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(
                title = stringResource(R.string.setting_policy_service),
                modifier = Modifier.clickable(onClick = onServiceClick)
            )
            Spacer(modifier = Modifier.height(24.dp))
            SettingArrow(
                title = stringResource(R.string.setting_policy_personal),
                modifier = Modifier.clickable(onClick = onPersonalClick)
            )
        }
    }

    if (showInstallationToken) {
        val clipboard = LocalClipboard.current

        BaseDialog {
            Text(installationToken)
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        versionClickCount = 0
                    }
                ) {
                    Text(
                        text = "닫기",
                    )
                }

                TextButton(
                    onClick = {
                        scope.launch {
                            val clipData = ClipData.newPlainText("설치 토큰", installationToken)
                            clipboard.setClipEntry(clipData.toClipEntry())
                        }
                    }
                ) {
                    Text(
                        text = "복사하기",
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingArrow(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = SoakTheme.typography.body18.copy(fontWeight = FontWeight.Medium)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_setting_arrow),
            contentDescription = "arrow button"
        )
    }
}

@Composable
private fun SettingText(
    title: String,
    subText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = SoakTheme.typography.body18.copy(fontWeight = FontWeight.Medium)
        )
        Text(
            text = subText,
            style = SoakTheme.typography.body16.copy(fontWeight = FontWeight.Normal)
        )
    }
}

private fun buttonClickEvent(jobGroup: List<String>, careerLevel: String) {
    // 맞춤정보 바텀시트_맞춤정보 보기 버튼 클릭
    Firebase.analytics.logEvent("click_bottom_sheet_custom") {
        param("object_type", "button")
        param("job_group", jobGroup.joinToString(separator = ","))
        param("career_level", careerLevel)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    SoakTheme {
        SettingScreen(
            onInfoUserClick = {},
            onNotificationClick = {},
            onBackClick = {},
            onServiceClick = {},
            onPersonalClick = {}
        )
    }
}
