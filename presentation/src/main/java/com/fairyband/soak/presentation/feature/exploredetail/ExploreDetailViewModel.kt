package com.fairyband.soak.presentation.feature.exploredetail

import androidx.lifecycle.ViewModel
import com.fairyband.soak.presentation.model.ExploreFeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExploreDetailViewModel : ViewModel() {
    // TODO: kotlin의 새로운 기능 확인해 보기
    private val _feeds = MutableStateFlow<List<ExploreFeed>>(emptyList())
    val feeds = _feeds.asStateFlow()

    private val _selectedIndex: MutableStateFlow<Int?> = MutableStateFlow(null)
    val selectedIndex = _selectedIndex.asStateFlow()

    // TODO: 상태로 초기화하기
    fun initialize(index: Int, feeds: List<ExploreFeed>) {
        _feeds.update { feeds }
        _selectedIndex.update { index }
    }
}