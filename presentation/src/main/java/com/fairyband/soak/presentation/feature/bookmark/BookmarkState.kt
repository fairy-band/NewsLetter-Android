package com.fairyband.soak.presentation.feature.bookmark

import com.fairyband.soak.presentation.model.BookmarkFeed

data class BookmarkState(
    val selectedTab: BookmarkTab = BookmarkTab.SAVED,
    val isLoading: Boolean = true,
    val feeds: List<BookmarkFeed> = emptyList(),
    val totalCount: Long = 0,
    val isSharedLoading: Boolean = true,
    val sharedFeeds: List<BookmarkFeed> = emptyList(),
    val sharedTotalCount: Long = 0,
    val bookmarkedIds: Set<Long> = emptySet(),
) {
    val isEmpty: Boolean
        get() = !isLoading && feeds.isEmpty()

    val isSharedEmpty: Boolean
        get() = !isSharedLoading && sharedFeeds.isEmpty()
}

enum class BookmarkTab {
    SAVED,

    /** 카카오톡 공유 링크로 받은 콘텐츠예요. */
    SHARED,
}
