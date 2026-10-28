package com.fairyband.soak.presentation.deeplink

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SharedLinkNavigatorTest : StringSpec({

    "요청 전에는 열 탭이 없다" {
        SharedLinkNavigator().openSharedTabRequest.value shouldBe null
    }

    "요청하면 등록할 콘텐츠 id 를 담아 둔다" {
        val navigator = SharedLinkNavigator()

        navigator.requestOpenSharedTab(exposureContentId = 42L)

        navigator.openSharedTabRequest.value shouldBe OpenSharedTabRequest(exposureContentId = 42L)
    }

    "링크에서 id 를 읽지 못해도 탭 열기 요청은 남는다" {
        val navigator = SharedLinkNavigator()

        navigator.requestOpenSharedTab(exposureContentId = null)

        navigator.openSharedTabRequest.value shouldBe OpenSharedTabRequest(exposureContentId = null)
    }

    "consume 하면 요청이 사라진다" {
        val navigator = SharedLinkNavigator()
        navigator.requestOpenSharedTab(exposureContentId = 42L)

        navigator.consume()

        navigator.openSharedTabRequest.value shouldBe null
    }
})
