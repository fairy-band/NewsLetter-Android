package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.local.SharedContent
import com.fairyband.soak.data.model.response.BookmarkResponse

data class BookmarkFeed(
    val exposureContentId: Long,
    val title: String,
    val keyword: String,
    val letter: String,
    val url: String,
)

fun BookmarkResponse.toBookmarkFeed(): BookmarkFeed {
    return BookmarkFeed(
        exposureContentId = exposureContentId,
        title = provocativeHeadline,
        keyword = provocativeKeyword,
        letter = newsletterName,
        url = contentUrl,
    )
}

fun SharedContent.toBookmarkFeed(): BookmarkFeed {
    return BookmarkFeed(
        exposureContentId = exposureContentId,
        title = title,
        keyword = keyword,
        letter = newsletterName,
        url = contentUrl,
    )
}
