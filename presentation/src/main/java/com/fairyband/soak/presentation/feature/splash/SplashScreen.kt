package com.fairyband.soak.presentation.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fairyband.soak.presentation.LocalNavController
import com.fairyband.soak.presentation.R
import com.fairyband.soak.presentation.navigation.Screen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(viewModel: SplashViewModel = koinViewModel()) {
    val isLoaded by viewModel.isLoaded.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    LaunchedEffect(isLoaded) {
        if (isLoaded) {
            navController.replace(Screen.Home)
        }
    }

    LaunchedEffect(Unit) {
        // remoteConfig를 못 받아와도 앱을 사용하는데 지장이 없기 때문에
        // 로딩이 안 끝나도 너무 오래 걸리면 그냥 홈으로 보내버린다.
        delay(5_000)
        navController.replace(Screen.Home)
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