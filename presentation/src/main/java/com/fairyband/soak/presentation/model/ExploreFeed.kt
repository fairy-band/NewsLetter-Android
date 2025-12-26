package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.response.ExploreContentResponse
import kotlinx.serialization.Serializable

@Serializable
data class ExploreFeed(
    val id: Int,
    val title: String,
    val keyword: String,
    val letter: String,
    val url: String,
    val summary: String,
)

fun ExploreContentResponse.toExploreFeed(): ExploreFeed {
    return ExploreFeed(
        id = id,
        title = provocativeHeadline,
        keyword = provocativeKeyword,
        letter = newsletterName,
        summary = summaryContent,
        url = contentUrl,
    )
}
