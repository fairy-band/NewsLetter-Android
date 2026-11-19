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
})
