package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.BookmarkResponse
import com.fairyband.soak.data.model.response.SharedContentResponse
import com.fairyband.soak.domain.model.NewsFeed

data class BookmarkFeed(
    val exposureContentId: Long,
    val title: String,
    val keyword: String,
    val letter: String,
    val url: String,
    val summary: String = "",
    val imageUrl: String? = null,
    val language: String = "",
    val category: String = "",
)

fun BookmarkResponse.toBookmarkFeed(): BookmarkFeed {
    return BookmarkFeed(
        exposureContentId = exposureContentId,
        title = provocativeHeadline,
        keyword = provocativeKeyword,
        letter = newsletterName,
        url = contentUrl,
        summary = summaryContent,
        imageUrl = imageUrl,
        language = language,
        category = categoryName,
    )
}

fun SharedContentResponse.toBookmarkFeed(): BookmarkFeed {
    return BookmarkFeed(
        exposureContentId = exposureContentId,
        title = provocativeHeadline,
        keyword = provocativeKeyword,
        letter = newsletterName,
        url = contentUrl,
        summary = summaryContent,
        imageUrl = imageUrl,
        language = language,
        category = categoryName,
    )
}

/**
 * 보관함에서도 추천 화면과 같은 팝업을 띄우기 위해 NewsFeed 로 바꿔요.
 */
fun BookmarkFeed.toNewsFeed(): NewsFeed {
    return NewsFeed(
        id = exposureContentId,
        title = title,
        keyword = keyword,
        letter = letter,
        summary = summary,
        url = url,
        imageUrl = imageUrl,
        language = language,
        cardType = "",
        category = category,
    )
}
