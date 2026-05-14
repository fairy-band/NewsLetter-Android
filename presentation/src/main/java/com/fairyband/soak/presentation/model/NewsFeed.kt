package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.NewsResponse

data class NewsFeed(
    val id: Long,
    val title: String,
    val keyword: String,
    val letter: String,
    val summary: String,
    val url: String,
    val imageUrl: String?,
    val language: String,
    val cardType: String,
)

fun NewsResponse.toNewsFeed(): NewsFeed {
    return NewsFeed(
        id = id,
        title = title,
        keyword = topKeyword,
        letter = newsletterName,
        summary = summary,
        url = contentUrl,
        imageUrl = imageUrl,
        language = language,
        cardType = cardType.getCardType(),
    )
}

private fun String.getCardType(): String {
    val upperType = this.uppercase()
    return when {
        upperType.startsWith("NEWS") -> "NEWS"
        upperType.startsWith("BLOG") -> "BLOG"
        upperType.startsWith("BOOK") -> "BOOK"
        else -> ""
    }
}
