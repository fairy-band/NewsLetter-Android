package com.fairyband.soak.presentation.feature.exploredetail

import androidx.lifecycle.ViewModel
import com.fairyband.soak.presentation.model.ExploreFeed
import com.fairyband.soak.presentation.navigation.MainDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreDetailViewModel(
    detail: MainDestination.ExploreDetail
) : ViewModel() {
    private val _feeds = MutableStateFlow<List<ExploreFeed>>(detail.feeds)
    val feeds = _feeds.asStateFlow()

    private val _selectedIndex = MutableStateFlow<Int?>(detail.index)
    val selectedIndex = _selectedIndex.asStateFlow()
}