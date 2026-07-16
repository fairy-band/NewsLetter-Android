package com.fairyband.soak.presentation.feature.home.bottomsheet

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PreferenceTest : StringSpec({
    "모든 직군은 stringValue 로 왕복 매핑된다" {
        Preference.entries.forEach { preference ->
            Preference.from(preference.stringValue) shouldBe preference
        }
    }

    "서버가 모르는 직군을 내려주면 null 을 반환한다" {
        Preference.from("QA") shouldBe null
    }

    "직군이 null 이면 null 을 반환한다" {
        Preference.from(null) shouldBe null
    }
})
