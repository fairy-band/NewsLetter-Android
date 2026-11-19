package com.fairyband.soak.presentation.deeplink

/**
 * 공유받은 콘텐츠로 앱을 여는 링크를 판별해요. 아래 세 형식을 받아요.
 *
 * - `kakao{NATIVE_APP_KEY}://kakaolink?exposureContentId={id}`: 예전 카카오톡 공유 메시지의 앱 실행 링크예요.
 *   카카오 콘솔의 메시지 템플릿(124946) 버튼 → Android/iOS 실행 파라미터에 `exposureContentId=${EXPOSURE_CONTENT_ID}` 로 설정돼 있어요.
 * - `soak://contents/share/{id}`: 웹 공유 페이지가 앱을 열 때 써요.
 * - `https://soak-app.duckdns.org/share/{id}`: 앱에서 공유하는 링크이자 앱 링크예요.
 *
 * 제목 등 나머지 정보는 공유받은 목록 API에서 받아와요.
 */
object SharedContentDeepLink {
    /** 카카오톡 공유 메시지로 앱을 실행할 때의 호스트예요. 스킴은 `kakao{NATIVE_APP_KEY}` 예요. */
    const val KAKAO_LINK_HOST = "kakaolink"

    const val PARAM_EXPOSURE_CONTENT_ID = "exposureContentId"

    /** 공유하는 쪽에서 [com.kakao.sdk.share.ShareClient]에 넘기는 템플릿 인자 이름이에요. */
    const val ARG_TITLE = "TITLE"
    const val ARG_EXPOSURE_CONTENT_ID = "EXPOSURE_CONTENT_ID"

    /** 공유 링크의 호스트예요. AndroidManifest 의 앱 링크 필터, 서버의 assetlinks 와 같아야 해요. */
    const val SHARE_HOST = "soak-app.duckdns.org"

    private const val SOAK_SCHEME = "soak"
    private const val SOAK_SHARE_HOST = "contents"
    private const val SHARE_PATH = "share"

    /** 콘텐츠를 공유할 때 보내는 링크예요. */
    fun shareUrlOf(exposureContentId: Long): String = "https://$SHARE_HOST/$SHARE_PATH/$exposureContentId"

    /**
     * 공유 링크면 [OpenSharedTabRequest] 를, 아니면 null 을 반환해요.
     * 공유 링크인데 콘텐츠 id 를 읽지 못하면 id 가 null 인 요청을 반환해 탭만 열어요.
     *
     * @param queryParameter 쿼리 파라미터 이름으로 디코딩된 값을 찾아요. 보통 `Uri::getQueryParameter` 를 넘겨요.
     */
    fun parse(
        scheme: String?,
        host: String?,
        pathSegments: List<String>,
        queryParameter: (String) -> String?,
    ): OpenSharedTabRequest? {
        val normalizedScheme = scheme?.lowercase() ?: return null
        val normalizedHost = host?.lowercase() ?: return null

        val isShareLink = when {
            normalizedHost == KAKAO_LINK_HOST && normalizedScheme.startsWith("kakao") ->
                return OpenSharedTabRequest(queryParameter(PARAM_EXPOSURE_CONTENT_ID)?.toLongOrNull())

            normalizedScheme == SOAK_SCHEME && normalizedHost == SOAK_SHARE_HOST -> true
            normalizedScheme == "https" && normalizedHost == SHARE_HOST -> true
            else -> false
        }
        if (!isShareLink || pathSegments.firstOrNull() != SHARE_PATH) return null

        return OpenSharedTabRequest(pathSegments.getOrNull(1)?.toLongOrNull())
    }
}
