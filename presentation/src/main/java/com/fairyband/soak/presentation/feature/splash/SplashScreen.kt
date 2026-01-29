package com.fairyband.soak.presentation.feature.splash

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.navigation.MainDestination
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(viewModel: SplashViewModel = koinViewModel()) {
    val isLoaded by viewModel.shouldGoHome.collectAsStateWithLifecycle()
    val shouldUpdate by viewModel.shouldUpdate.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(isLoaded) {
        if (isLoaded) {
            navController.replace(MainDestination.Home)
        }
    }

    if (shouldUpdate) {
        ForceUpdateDialog()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            imageVector = ImageVector.vectorResource(R.drawable.symbol),
            contentDescription = null,
        )
    }
}

@Composable
private fun ForceUpdateDialog() {
    val context = LocalContext.current
    val activity = LocalActivity.current

    Dialog(
        onDismissRequest = {
            activity?.finish()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.splash_update_title),
                    style = SoakTheme.typography.body18.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.splash_update_description),
                    style = SoakTheme.typography.body16,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = "market://details?id=${context.packageName}".toUri()
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.splash_update))
                }
            }
        }
    }
}