package com.fairyband.soak.presentation.feature.explore

import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class ExploreJobFilterTest : StringSpec({

    "전체 상태에서 직군을 고르면 그 직군만 선택된다" {
        selectJobFilter(current = emptyList(), preference = Preference.ANDROID) shouldBe listOf(Preference.ANDROID)
    }

    "다른 직군을 고르면 이전 선택은 풀리고 새 직군만 선택된다" {
        selectJobFilter(current = listOf(Preference.ANDROID), preference = Preference.IOS) shouldBe listOf(Preference.IOS)
    }

    "선택된 직군을 다시 누르면 전체로 돌아간다" {
        selectJobFilter(current = listOf(Preference.ANDROID), preference = Preference.ANDROID).shouldBeEmpty()
    }
})
