package com.fairyband.soak.presentation.feature.explore

import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import com.fairyband.soak.presentation.model.ExploreFeed

data class ExploreState(
    val feeds: List<ExploreFeed> = emptyList(),
    val totalCount: Int = 0,
    val name: String = "",
    val url: String = "",
    val selectedPreferences: List<Preference> = emptyList(),
    val language: String = "",
)
