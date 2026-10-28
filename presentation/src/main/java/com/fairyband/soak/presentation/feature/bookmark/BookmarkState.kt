package com.fairyband.soak.presentation.feature.bookmark

import com.fairyband.soak.presentation.model.BookmarkFeed

data class BookmarkState(
    val selectedTab: BookmarkTab = BookmarkTab.SAVED,
    val isLoading: Boolean = true,
    val feeds: List<BookmarkFeed> = emptyList(),
    val totalCount: Long = 0,
    val sharedFeeds: List<BookmarkFeed> = emptyList(),
    val bookmarkedIds: Set<Long> = emptySet(),
) {
    val isEmpty: Boolean
        get() = !isLoading && feeds.isEmpty()
}

enum class BookmarkTab {
    SAVED,

    /** 카카오톡 공유 링크로 받은 콘텐츠예요. 서버 API가 없어서 로컬에 저장한 목록을 보여줘요. */
    SHARED,
}
