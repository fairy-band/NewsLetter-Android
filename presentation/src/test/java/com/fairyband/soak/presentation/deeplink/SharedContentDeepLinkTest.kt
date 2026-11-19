package com.fairyband.soak.presentation.deeplink

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URI

/** Android `Uri` 대신 `java.net.URI` 로 링크를 쪼개 판별해요. */
private fun parse(link: String): OpenSharedTabRequest? {
    val uri = URI(link)
    val query = uri.rawQuery.orEmpty()
        .split("&")
        .filter { it.isNotEmpty() }
        .associate { it.substringBefore("=") to it.substringAfter("=", "") }
    return SharedContentDeepLink.parse(
        scheme = uri.scheme,
        host = uri.host ?: uri.authority,
        pathSegments = uri.path.orEmpty().split("/").filter { it.isNotEmpty() },
        queryParameter = query::get,
    )
}

private fun sharedTab(id: Long?) = OpenSharedTabRequest(exposureContentId = id)

class SharedContentDeepLinkTest : StringSpec({

    "카카오 링크에서 exposureContentId 를 숫자로 읽는다" {
        parse("kakao1234://kakaolink?exposureContentId=42&title=abc") shouldBe sharedTab(42L)
    }

    "카카오 링크에 id 가 없거나 숫자가 아니면 탭만 연다" {
        parse("kakao1234://kakaolink?title=abc") shouldBe sharedTab(null)
        parse("kakao1234://kakaolink?exposureContentId=abc") shouldBe sharedTab(null)
    }

    "soak 스킴 공유 링크에서 id 를 읽는다" {
        parse("soak://contents/share/42") shouldBe sharedTab(42L)
    }

    "soak 스킴 공유 링크에 id 가 없거나 숫자가 아니면 탭만 연다" {
        parse("soak://contents/share/abc") shouldBe sharedTab(null)
        parse("soak://contents/share") shouldBe sharedTab(null)
    }

    "공유 경로가 아닌 soak 링크는 처리하지 않는다" {
        // `soak://` 는 java.net.URI 가 해석하지 못해서 Android Uri 가 주는 값(호스트 없음)으로 직접 넘겨요.
        SharedContentDeepLink.parse(
            scheme = "soak",
            host = null,
            pathSegments = emptyList(),
            queryParameter = { null },
        ) shouldBe null
        parse("soak://contents/other/42") shouldBe null
        parse("soak://foo/share/42") shouldBe null
    }

    "https 공유 링크에서 id 를 읽는다" {
        parse("https://soak-app.duckdns.org/share/42") shouldBe sharedTab(42L)
        parse("https://soak-app.duckdns.org/share/42/") shouldBe sharedTab(42L)
    }

    "스킴과 호스트의 대소문자는 구분하지 않는다" {
        parse("SOAK://CONTENTS/share/42") shouldBe sharedTab(42L)
        parse("HTTPS://SOAK-APP.DUCKDNS.ORG/share/42") shouldBe sharedTab(42L)
    }

    "다른 호스트나 경로의 https 링크는 처리하지 않는다" {
        parse("https://example.com/share/42") shouldBe null
        parse("https://soak-app.duckdns.org/contents/42") shouldBe null
        parse("http://soak-app.duckdns.org/share/42") shouldBe null
    }

    "공유 링크를 만든다" {
        SharedContentDeepLink.shareUrlOf(42L) shouldBe "https://soak-app.duckdns.org/share/42"
    }

    "만든 공유 링크를 다시 판별하면 같은 id 가 나온다" {
        parse(SharedContentDeepLink.shareUrlOf(17440L)) shouldBe sharedTab(17440L)
    }
})
