package com.fairyband.soak.presentation.feature.home

import com.fairyband.soak.core.theme.SoakColors
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CardColorsTest : StringSpec({
    val soakColors = SoakColors()

    "직군에 따라 카드 색이 정해진다" {
        cardColorOf("BE").cardColor shouldBe soakColors.purpleBackgroundPrimary
        cardColorOf("FE").cardColor shouldBe soakColors.mintBackgroundPrimary
        cardColorOf("Android").cardColor shouldBe soakColors.greenBackgroundPrimary
        cardColorOf("iOS").cardColor shouldBe soakColors.pinkBackgroundPrimary
        cardColorOf("DevOps").cardColor shouldBe soakColors.blueBackgroundPrimary
        cardColorOf("AI").cardColor shouldBe soakColors.lemonYellowBackgroundPrimary
    }

    "직군 이름의 대소문자는 구분하지 않는다" {
        cardColorOf("devops") shouldBe cardColorOf("DevOps")
    }

    "모르는 직군이거나 비어 있으면 오렌지로 보여준다" {
        cardColorOf("Unknown").cardColor shouldBe soakColors.orangeBackgroundPrimary
        cardColorOf("").cardColor shouldBe soakColors.orangeBackgroundPrimary
    }
})
