package com.fairyband.soak.presentation.deeplink

import com.fairyband.soak.data.model.local.SharedContent

/**
 * 카카오톡 공유 메시지의 앱 실행 링크(`kakao{KEY}://kakaolink?...`)에 담기는 콘텐츠 정보예요.
 *
 * 카카오 콘솔의 메시지 템플릿(124946) 버튼 → Android/iOS 실행 파라미터에 아래처럼 설정해야 해요.
 * `exposureContentId=${EXPOSURE_CONTENT_ID}&title=${TITLE}&keyword=${KEYWORD}&newsletterName=${NEWSLETTER}&contentUrl=${URL}`
 */
object SharedContentDeepLink {
    /** 카카오톡 공유 메시지로 앱을 실행할 때의 호스트예요. 스킴은 `kakao{NATIVE_APP_KEY}` 예요. */
    const val KAKAO_LINK_HOST = "kakaolink"

    const val PARAM_EXPOSURE_CONTENT_ID = "exposureContentId"
    const val PARAM_TITLE = "title"
    const val PARAM_KEYWORD = "keyword"
    const val PARAM_NEWSLETTER_NAME = "newsletterName"
    const val PARAM_CONTENT_URL = "contentUrl"

    /** 공유하는 쪽에서 [com.kakao.sdk.share.ShareClient]에 넘기는 템플릿 인자 이름이에요. */
    const val ARG_TITLE = "TITLE"
    const val ARG_EXPOSURE_CONTENT_ID = "EXPOSURE_CONTENT_ID"
    const val ARG_KEYWORD = "KEYWORD"
    const val ARG_NEWSLETTER = "NEWSLETTER"
    const val ARG_URL = "URL"

    /**
     * 콘텐츠를 다시 열 수 있는 최소 정보(id, 제목)가 없으면 null 을 반환해요.
     *
     * @param queryParameter 쿼리 파라미터 이름으로 디코딩된 값을 찾아요. 보통 `Uri::getQueryParameter` 를 넘겨요.
     */
    fun parse(
        queryParameter: (String) -> String?,
        receivedAt: Long,
    ): SharedContent? {
        val exposureContentId = queryParameter(PARAM_EXPOSURE_CONTENT_ID)?.toLongOrNull() ?: return null
        val title = queryParameter(PARAM_TITLE)?.takeIf { it.isNotBlank() } ?: return null

        return SharedContent(
            exposureContentId = exposureContentId,
            title = title,
            keyword = queryParameter(PARAM_KEYWORD).orEmpty(),
            newsletterName = queryParameter(PARAM_NEWSLETTER_NAME).orEmpty(),
            contentUrl = queryParameter(PARAM_CONTENT_URL).orEmpty(),
            receivedAt = receivedAt,
        )
    }
}
