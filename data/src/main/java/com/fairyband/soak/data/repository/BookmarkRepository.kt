package com.fairyband.soak.data.repository

import com.fairyband.soak.data.model.response.BookmarksResponse
import kotlinx.coroutines.flow.StateFlow

interface BookmarkRepository {
    /**
     * 북마크한 exposureContentId 목록이에요.
     * 홈 콘텐츠 응답에는 북마크 여부가 없어서, 북마크 목록 API로 동기화해요.
     */
    val bookmarkedIds: StateFlow<Set<Long>>

    suspend fun syncBookmarkedIds()

    /**
     * @param page 0부터 시작해요.
     */
    suspend fun getBookmarks(page: Int): BookmarksResponse

    /**
     * 북마크 상태를 먼저 반영하고, 요청이 실패하면 되돌린 뒤 예외를 다시 던져요.
     */
    suspend fun updateBookmark(exposureContentId: Long, isBookmarked: Boolean)
}
