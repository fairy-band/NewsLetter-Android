package com.nexters.knownknowns.presentation.model

import com.nexters.knownknowns.data.model.NewsResponse

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
        id = id,
        title = title,
        keyword = keyword,
        letter = letter,
        summary = summary,
        url = url,
    )
}
