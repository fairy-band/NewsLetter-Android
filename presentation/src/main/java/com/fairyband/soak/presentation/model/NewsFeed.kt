package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.NewsResponse

data class NewsFeed(
    val id: String,
    val title: String,
    val keyword: String,
    val letter: String,
    val summary: String,
    val url: String,
)

fun NewsResponse.toNewsFeed(): NewsFeed {
    return NewsFeed(
        id = contentUrl,
        title = title,
        keyword = topKeyword,
        letter = newsletterName,
        summary = summary,
        url = contentUrl,
    )
}
