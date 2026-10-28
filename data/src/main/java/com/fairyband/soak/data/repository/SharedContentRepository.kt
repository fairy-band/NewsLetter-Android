package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.response.SharedContentsResponse

interface SharedContentRepository {
    /**
     * 최근에 공유받은 순으로 정렬돼 있어요.
     *
     * @param page 0부터 시작해요.
     */
    suspend fun getSharedContents(page: Int): SharedContentsResponse

    /**
     * 카카오톡 공유 링크로 받은 콘텐츠를 공유받은 목록에 등록해요.
     */
    suspend fun addSharedContent(exposureContentId: Long)
}
