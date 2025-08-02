package com.nexters.knownknowns.presentation.feature.home

import androidx.compose.ui.graphics.Color
import com.nexters.knownknowns.core.theme.KnownKnownsColors
import java.time.LocalDate
import kotlin.random.Random

private val soakColors = KnownKnownsColors()

fun getCardColors(colorType: String, keywords: List<String>): List<Color> {
    return if (colorType == "A") generateCardColorA(keywords) else cardColorsB
}

fun getCardTitleColors(colorType: String, keywords: List<String>): List<Color> {
    return if (colorType == "A") generateCardTextColorA(keywords) else cardTitleColors
}

private val aColorSet = listOf(
    listOf(
        soakColors.blueBackgroundPrimary,
        soakColors.blueBackgroundSecondary,
        soakColors.blueBackgroundTertiary
    ),
    listOf(
        soakColors.orangeBackgroundPrimary,
        soakColors.orangeBackgroundSecondary,
        soakColors.orangeBackgroundTertiary
    ),
    listOf(
        soakColors.pinkBackgroundPrimary,
        soakColors.pinkBackgroundSecondary,
        soakColors.pinkBackgroundTertiary
    ),
    listOf(
        soakColors.purpleBackgroundPrimary,
        soakColors.purpleBackgroundSecondary,
        soakColors.purpleBackgroundTertiary
    ),
    listOf(
        soakColors.greenBackgroundPrimary,
        soakColors.greenBackgroundSecondary,
        soakColors.greenBackgroundTertiary
    ),
    listOf(
        soakColors.lemonYellowBackgroundPrimary,
        soakColors.lemonYellowBackgroundSecondary,
        soakColors.lemonYellowBackgroundTertiary
    ),
)

private val cardColorsB = listOf(
    soakColors.greenBackgroundPrimary,
    soakColors.pinkBackgroundPrimary,
    soakColors.lemonYellowBackgroundPrimary,
    soakColors.blueBackgroundPrimary,
    soakColors.orangeBackgroundPrimary,
    soakColors.purpleBackgroundPrimary,
)

private val cardTitleColors = listOf(
    soakColors.greenText,
    soakColors.pinkText,
    soakColors.lemonYellowText,
    soakColors.blueText,
    soakColors.orangeText,
    soakColors.purpleText,
)

/**
 * 같은 날이면 동일한 seed 값을 이용하여 여러 번 호출해도 동일한 결과가 나온다.
 * 따라서 오늘의 색깔을 별도로 저장하거나 전달할 필요가 없다.
 */
fun generateCardColorA(keywords: List<String>): List<Color> {
    val today = LocalDate.now()
    val seed = today.year * 10000 + today.monthValue * 100 + today.dayOfMonth

    val randomColorSet = aColorSet.shuffled(Random(seed))
    val keywordNumberMap = mutableMapOf<String, Int>()
    var cnt = 0
    for (keyword in keywords) {
        if (keywordNumberMap[keyword] == null) {
            keywordNumberMap[keyword] = cnt++
        }
    }

    return keywords.mapIndexed { index, keyword ->
        val keywordNumber = keywordNumberMap[keyword] ?: 5
        randomColorSet[keywordNumber][index % 3]
    }
}

/**
 * 같은 날이면 동일한 seed 값을 이용하여 여러 번 호출해도 동일한 결과가 나온다.
 * 따라서 오늘의 색깔을 별도로 저장하거나 전달할 필요가 없다.
 */
fun generateCardTextColorA(keywords: List<String>): List<Color> {
    val today = LocalDate.now()
    val seed = today.year * 10000 + today.monthValue * 100 + today.dayOfMonth

    val randomColorSet = cardTitleColors.shuffled(Random(seed))
    val keywordNumberMap = mutableMapOf<String, Int>()
    var cnt = 0
    for (keyword in keywords) {
        if (keywordNumberMap[keyword] == null) {
            keywordNumberMap[keyword] = cnt++
        }
    }

    return keywords.map { keyword ->
        val keywordNumber = keywordNumberMap[keyword] ?: 5
        randomColorSet[keywordNumber]
    }
}