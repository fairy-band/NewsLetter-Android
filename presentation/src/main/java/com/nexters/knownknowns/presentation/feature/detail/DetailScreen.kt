package com.nexters.knownknowns.presentation.feature.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    title: String,
    viewModel: DetailViewModel = koinViewModel(),
) {
    DetailScreen(title1 = title)
}

@Composable
fun DetailScreen(title1: String) {
    Text(title1, color = Color.Blue)
}