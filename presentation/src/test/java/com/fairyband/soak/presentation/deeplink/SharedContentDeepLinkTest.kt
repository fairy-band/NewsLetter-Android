package com.fairyband.soak.presentation.deeplink

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private fun parse(vararg params: Pair<String, String>) =
    SharedContentDeepLink.parse(queryParameter = params.toMap()::get)

class SharedContentDeepLinkTest : StringSpec({

    "exposureContentId 를 숫자로 읽는다" {
        parse(
            "exposureContentId" to "42",
            "title" to "제목",
            "keyword" to "Kotlin",
            "newsletterName" to "안드로이드 위클리",
            "contentUrl" to "https://example.com",
        ) shouldBe 42L
    }

    "제목 등 다른 파라미터가 없어도 exposureContentId 만 있으면 읽는다" {
        parse("exposureContentId" to "42") shouldBe 42L
    }

    "exposureContentId 가 없거나 숫자가 아니면 null 이다" {
        parse("title" to "제목") shouldBe null
        parse("exposureContentId" to "abc", "title" to "제목") shouldBe null
    }
})
