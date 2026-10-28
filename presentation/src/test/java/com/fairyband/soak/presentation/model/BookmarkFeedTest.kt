package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.SharedContentResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BookmarkFeedTest : StringSpec({

    "공유받은 콘텐츠 응답을 보관함 카드로 변환한다" {
        val response = SharedContentResponse(
            sharedContentId = 1,
            exposureContentId = 10500,
            contentId = 1420,
            provocativeKeyword = "#AI #MCP",
            provocativeHeadline = "Chrome DevTools MCP",
            summaryContent = "요약",
            contentUrl = "https://example.com",
            newsletterName = "GeekNews",
            categoryName = "BE",
            language = "KOREAN",
            sharedAt = "2026-10-29T10:00:00",
            createdAt = "2026-10-28T10:00:00",
        )

        response.toBookmarkFeed() shouldBe BookmarkFeed(
            exposureContentId = 10500,
            title = "Chrome DevTools MCP",
            keyword = "#AI #MCP",
            letter = "GeekNews",
            url = "https://example.com",
        )
    }
})
