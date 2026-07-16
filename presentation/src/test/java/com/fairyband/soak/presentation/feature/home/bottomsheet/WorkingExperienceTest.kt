package com.fairyband.soak.presentation.feature.home.bottomsheet

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class WorkingExperienceTest : StringSpec({
    "모든 경력은 stringValue 로 왕복 매핑된다" {
        WorkingExperience.entries.forEach { workingExperience ->
            WorkingExperience.from(workingExperience.stringValue) shouldBe workingExperience
        }
    }

    "서버가 모르는 경력을 내려주면 null 을 반환한다" {
        WorkingExperience.from("INTERN") shouldBe null
    }

    "아직 경력을 선택하지 않은 사용자는 null 을 반환한다" {
        WorkingExperience.from(null) shouldBe null
    }
})
