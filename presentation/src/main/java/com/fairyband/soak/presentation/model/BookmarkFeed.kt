package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.BookmarkResponse
import com.fairyband.soak.data.model.response.SharedContentResponse

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

fun SharedContentResponse.toBookmarkFeed(): BookmarkFeed {
    return BookmarkFeed(
        exposureContentId = exposureContentId,
        title = provocativeHeadline,
        keyword = provocativeKeyword,
        letter = newsletterName,
        url = contentUrl,
    )
}
