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

/**
 * 카드 색은 콘텐츠의 직군(서버의 categoryName)으로 정해요. 모르는 직군은 오렌지로 보여줘요.
 */
fun cardColorOf(category: String): CardColor = when (category.lowercase()) {
    "be" -> purpleCardColor
    "fe" -> mintCardColor
    "android" -> greenCardColor
    "ios" -> pinkCardColor
    "devops" -> blueCardColor
    "ai" -> lemonYellowCardColor
    else -> orangeCardColor
}

private val blueCardColor = CardColor(
    titleColor = soakColors.blueText,
    cardColor = soakColors.blueBackgroundPrimary,
    imageTextColor = soakColors.blueTextPrimary
)

private val lemonYellowCardColor = CardColor(
    titleColor = soakColors.lemonYellowText,
    cardColor = soakColors.lemonYellowBackgroundPrimary,
    imageTextColor = soakColors.lemonYellowTextPrimary
)

private val purpleCardColor = CardColor(
    titleColor = soakColors.purpleText,
    cardColor = soakColors.purpleBackgroundPrimary,
    imageTextColor = soakColors.purpleTextPrimary
)

private val mintCardColor = CardColor(
    titleColor = soakColors.mintText,
    cardColor = soakColors.mintBackgroundPrimary,
    imageTextColor = soakColors.mintTextPrimary
)

private val pinkCardColor = CardColor(
    titleColor = soakColors.pinkText,
    cardColor = soakColors.pinkBackgroundPrimary,
    imageTextColor = soakColors.pinkTextPrimary
)

private val greenCardColor = CardColor(
    titleColor = soakColors.greenText,
    cardColor = soakColors.greenBackgroundPrimary,
    imageTextColor = soakColors.greenTextPrimary
)

private val orangeCardColor = CardColor(
    titleColor = soakColors.orangeText,
    cardColor = soakColors.orangeBackgroundPrimary,
    imageTextColor = soakColors.orangeTextPrimary
)

private val cardColors = listOf(
    blueCardColor,
    lemonYellowCardColor,
    purpleCardColor,
    mintCardColor,
    pinkCardColor,
    greenCardColor,
    orangeCardColor,
)
