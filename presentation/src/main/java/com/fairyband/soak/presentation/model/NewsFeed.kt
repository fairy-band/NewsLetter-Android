package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.NewsResponse

data class NewsFeed(
    val id: Int,
    val title: String,
    val keyword: String,
    val letter: String,
    val summary: String,
    val url: String,
)

fun NewsResponse.toNewsFeed(): NewsFeed {
    return NewsFeed(
        id = id,
        title = title,
        keyword = topKeyword,
        letter = newsletterName,
        summary = summary,
        url = contentUrl,
    )
}
