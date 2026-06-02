package com.fairyband.soak.presentation.feature.home

import androidx.compose.ui.graphics.Color
import com.fairyband.soak.core.theme.SoakColors

data class CardColor(
    val titleColor: Color,
    val imageTextColor: Color,
    val cardColor: Color,
)

private val soakColors = SoakColors()

fun getCardColors(): List<CardColor> = cardColors

private val cardColors = listOf(
    CardColor(
        titleColor = soakColors.blueText,
        cardColor = soakColors.blueBackgroundPrimary,
        imageTextColor = soakColors.blueTextPrimary
    ),
    CardColor(
        titleColor = soakColors.lemonYellowText,
        cardColor = soakColors.lemonYellowBackgroundPrimary,
        imageTextColor = soakColors.lemonYellowTextPrimary
    ),
    CardColor(
        titleColor = soakColors.purpleText,
        cardColor = soakColors.purpleBackgroundPrimary,
        imageTextColor = soakColors.purpleTextPrimary
    ),
    CardColor(
        titleColor = soakColors.mintText,
        cardColor = soakColors.mintBackgroundPrimary,
        imageTextColor = soakColors.mintTextPrimary
    ),
    CardColor(
        titleColor = soakColors.pinkText,
        cardColor = soakColors.pinkBackgroundPrimary,
        imageTextColor = soakColors.pinkTextPrimary
    ),
    CardColor(
        titleColor = soakColors.greenText,
        cardColor = soakColors.greenBackgroundPrimary,
        imageTextColor = soakColors.greenTextPrimary
    ),
    CardColor(
        titleColor = soakColors.orangeText,
        cardColor = soakColors.orangeBackgroundPrimary,
        imageTextColor = soakColors.orangeTextPrimary
    ),
)
