package com.fairyband.soak.presentation.feature.bookmark

import com.fairyband.soak.presentation.model.BookmarkFeed

data class BookmarkState(
    val selectedTab: BookmarkTab = BookmarkTab.SAVED,
    val isLoading: Boolean = true,
    val feeds: List<BookmarkFeed> = emptyList(),
    val totalCount: Long = 0,
) {
    val isEmpty: Boolean
        get() = !isLoading && feeds.isEmpty()
}

enum class BookmarkTab {
    SAVED,

    // TODO: 딥링크로 받은 공유 콘텐츠를 로컬에 저장해 보여줄 예정이에요. 지금은 항상 빈 상태예요.
    SHARED,
}
