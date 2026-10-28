package com.fairyband.soak.presentation.deeplink

/**
 * 카카오톡 공유 메시지의 앱 실행 링크(`kakao{KEY}://kakaolink?...`)에 담기는 콘텐츠 정보예요.
 *
 * 카카오 콘솔의 메시지 템플릿(124946) 버튼 → Android/iOS 실행 파라미터에 아래처럼 설정해야 해요.
 * `exposureContentId=${EXPOSURE_CONTENT_ID}`
 * 제목 등 나머지 정보는 공유받은 목록 API에서 받아와요.
 */
object SharedContentDeepLink {
    /** 카카오톡 공유 메시지로 앱을 실행할 때의 호스트예요. 스킴은 `kakao{NATIVE_APP_KEY}` 예요. */
    const val KAKAO_LINK_HOST = "kakaolink"

    const val PARAM_EXPOSURE_CONTENT_ID = "exposureContentId"

    /** 공유하는 쪽에서 [com.kakao.sdk.share.ShareClient]에 넘기는 템플릿 인자 이름이에요. */
    const val ARG_TITLE = "TITLE"
    const val ARG_EXPOSURE_CONTENT_ID = "EXPOSURE_CONTENT_ID"

    /**
     * 공유받은 콘텐츠의 exposureContentId 를 반환해요. 없거나 숫자가 아니면 null 이에요.
     *
     * @param queryParameter 쿼리 파라미터 이름으로 디코딩된 값을 찾아요. 보통 `Uri::getQueryParameter` 를 넘겨요.
     */
    fun parse(queryParameter: (String) -> String?): Long? {
        return queryParameter(PARAM_EXPOSURE_CONTENT_ID)?.toLongOrNull()
    }
}
