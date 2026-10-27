package com.fairyband.soak.data.model.local

import kotlinx.serialization.Serializable

/**
 * 카카오톡 공유 링크로 받은 콘텐츠예요.
 * 공유받은 목록을 조회하는 API가 없어서, 딥링크로 받은 정보를 그대로 로컬에 저장해요.
 *
 * @param receivedAt 공유받은 시각(epoch millis)이에요. 최근에 받은 순으로 정렬할 때 사용해요.
 */
@Serializable
data class SharedContent(
    val exposureContentId: Long,
    val title: String,
    val keyword: String,
    val newsletterName: String,
    val contentUrl: String,
    val receivedAt: Long,
)
