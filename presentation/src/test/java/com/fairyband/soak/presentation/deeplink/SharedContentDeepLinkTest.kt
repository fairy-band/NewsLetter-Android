package com.fairyband.soak.presentation.deeplink

import com.fairyband.soak.data.model.local.SharedContent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private fun parse(vararg params: Pair<String, String>) =
    SharedContentDeepLink.parse(queryParameter = params.toMap()::get, receivedAt = 1L)

class SharedContentDeepLinkTest : StringSpec({

    "모든 파라미터가 있으면 공유받은 콘텐츠로 변환한다" {
        parse(
            "exposureContentId" to "42",
            "title" to "제목",
            "keyword" to "Kotlin",
            "newsletterName" to "안드로이드 위클리",
            "contentUrl" to "https://example.com",
        ) shouldBe SharedContent(
            exposureContentId = 42,
            title = "제목",
            keyword = "Kotlin",
            newsletterName = "안드로이드 위클리",
            contentUrl = "https://example.com",
            receivedAt = 1L,
        )
    }

    "선택 파라미터가 없으면 빈 값으로 채운다" {
        parse("exposureContentId" to "42", "title" to "제목") shouldBe SharedContent(
            exposureContentId = 42,
            title = "제목",
            keyword = "",
            newsletterName = "",
            contentUrl = "",
            receivedAt = 1L,
        )
    }

    "exposureContentId 가 없거나 숫자가 아니면 null 이다" {
        parse("title" to "제목") shouldBe null
        parse("exposureContentId" to "abc", "title" to "제목") shouldBe null
    }

    "제목이 없으면 null 이다" {
        parse("exposureContentId" to "42") shouldBe null
        parse("exposureContentId" to "42", "title" to "") shouldBe null
    }
})
