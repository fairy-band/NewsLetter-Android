package com.fairyband.soak.presentation.feature.explore

import com.fairyband.soak.data.model.request.Direction
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import com.fairyband.soak.presentation.model.ExploreFeed

data class ExploreState(
    val feeds: List<ExploreFeed> = emptyList(),
    val totalCount: Int = 0,
    val direction: Direction = Direction.DESC,
    val selectedJobFilters: List<Preference> = emptyList(),
    val name: String = "",
    val url: String = "",
    val selectedPreferences: List<Preference> = emptyList(),
    val language: String = "",
) {
    /**
     * 직군을 하나도 고르지 않았거나 모두 고른 상태는 '전체' 로 취급해요.
     */
    val isAllJobFiltersSelected: Boolean
        get() = selectedJobFilters.isEmpty()

    val isSubmitEnabled: Boolean
        get() = name.isNotBlank() && url.isNotBlank() && selectedPreferences.isNotEmpty() && language.isNotBlank()
}
