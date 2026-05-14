package com.fairyband.soak.presentation.feature.home

import androidx.compose.ui.graphics.Color
import com.fairyband.soak.core.theme.SoakColors
import java.time.LocalDate
import kotlin.random.Random

data class CardColor(
    val textColor: Color,
    val cardColor: Color,
)

private val soakColors = SoakColors()

fun getCardColors(colorType: String, keywords: List<String>): List<CardColor> {
    return if (colorType == "A") {
        val backgrounds = generateCardColorA(keywords)
        val textColors = generateCardTextColorA(keywords)
        backgrounds.zip(textColors).map { (bg, text) -> CardColor(textColor = bg, cardColor = text) }
    } else {
        cardColorsB
    }
}

fun getCardTitleColors(colorType: String, keywords: List<String>): List<Color> {
    return getCardColors(colorType, keywords).map { it.cardColor }
}

private val aColorSet = listOf(
    listOf(
        soakColors.greenBackgroundPrimary,
        soakColors.greenBackgroundSecondary,
        soakColors.greenBackgroundTertiary
    ),
    listOf(
        soakColors.pinkBackgroundPrimary,
        soakColors.pinkBackgroundSecondary,
        soakColors.pinkBackgroundTertiary
    ),
    listOf(
        soakColors.lemonYellowBackgroundPrimary,
        soakColors.lemonYellowBackgroundSecondary,
        soakColors.lemonYellowBackgroundTertiary
    ),
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
        soakColors.purpleBackgroundPrimary,
        soakColors.purpleBackgroundSecondary,
        soakColors.purpleBackgroundTertiary
    ),
)

private val cardColorsB = listOf(
    CardColor(cardColor = soakColors.greenBackgroundPrimary, textColor = soakColors.greenTextPrimary),
    CardColor(cardColor = soakColors.pinkBackgroundPrimary, textColor = soakColors.pinkTextPrimary),
    CardColor(cardColor = soakColors.lemonYellowBackgroundPrimary, textColor = soakColors.lemonYellowTextPrimary),
    CardColor(cardColor = soakColors.blueBackgroundPrimary, textColor = soakColors.blueTextPrimary),
    CardColor(cardColor = soakColors.orangeBackgroundPrimary, textColor = soakColors.orangeTextPrimary),
    CardColor(cardColor = soakColors.purpleBackgroundPrimary, textColor = soakColors.purpleTextPrimary),
)

/**
 * 오늘의 색상 순서를 만든다.
 */
private fun getTodayColorIndexes(keywords: List<String>): List<Int> {
    val today = LocalDate.now()
    val seed = today.year * 10000 + today.monthValue * 100 + today.dayOfMonth

    return (0..5).shuffled(Random(seed))
}

/**
 * 각각의 키워드를 몇 번째 색상에 매핑할 것인지 결정한다.
 */
private fun getKeywordIndexMap(keywords: List<String>): Map<String, Int> {
    val keywordNumberMap = mutableMapOf<String, Int>()
    var cnt = 0
    for (keyword in keywords) {
        if (keywordNumberMap[keyword] == null) {
            keywordNumberMap[keyword] = cnt++
        }
    }

    return keywordNumberMap
}

/**
 * 같은 날이면 동일한 seed 값을 이용하여 여러 번 호출해도 동일한 결과가 나온다.
 * 따라서 오늘의 색깔을 별도로 저장하거나 전달할 필요가 없다.
 */
fun generateCardColorA(keywords: List<String>): List<Color> {
    val colorIndexes = getTodayColorIndexes(keywords)
    val keywordIndexMap = getKeywordIndexMap(keywords)
    val visited = IntArray(6)

    return keywords.mapIndexed { index, keyword ->
        val keywordIndex = keywordIndexMap[keyword] ?: 5
        val colorIndex = colorIndexes[keywordIndex]
        aColorSet[colorIndex][visited[colorIndex]++ % 3]
    }
}

/**
 * 같은 날이면 동일한 seed 값을 이용하여 여러 번 호출해도 동일한 결과가 나온다.
 * 따라서 오늘의 색깔을 별도로 저장하거나 전달할 필요가 없다.
 */
fun generateCardTextColorA(keywords: List<String>): List<Color> {
    val colorIndexes = getTodayColorIndexes(keywords)
    val keywordIndexMap = getKeywordIndexMap(keywords)
    val textColors = listOf(
        soakColors.greenText,
        soakColors.pinkText,
        soakColors.lemonYellowText,
        soakColors.blueText,
        soakColors.orangeText,
        soakColors.purpleText,
    )

    return keywords.map { keyword ->
        val keywordIndex = keywordIndexMap[keyword] ?: 5
        val colorIndex = colorIndexes[keywordIndex]
        textColors[colorIndex]
    }
}