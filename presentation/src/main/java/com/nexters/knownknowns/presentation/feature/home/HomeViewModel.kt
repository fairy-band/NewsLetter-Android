package com.nexters.knownknowns.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.knownknowns.data.repository.NewsRepository
import com.nexters.knownknowns.data.repository.RemoteConfigRepository
import com.nexters.knownknowns.domain.usecase.BottomSheetUseCase
import com.nexters.knownknowns.presentation.model.NewsFeed
import com.nexters.knownknowns.presentation.model.toNewsFeed
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val newsRepository: NewsRepository,
    private val bottomSheetUseCase: BottomSheetUseCase,
    remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeSideEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    val news: StateFlow<ImmutableList<NewsFeed>> = newsRepository
        .getNews()
        .map {
            it.map { response ->
                response.toNewsFeed()
            }.toImmutableList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            persistentListOf()
        )
    val colorType = remoteConfigRepository
        .getColor()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            "static"
        )

    init {
        observeBottomSheetTrigger()
    }

    private fun observeBottomSheetTrigger() {
        viewModelScope.launch {
            bottomSheetUseCase.shouldShowBottomSheet
                .filter { it }
                .collect {
                    _eventFlow.emit(HomeSideEffect.ShowBottomSheet)
                    onBottomSheetShown()
                }
        }
    }

    fun onNewsClicked() {
        viewModelScope.launch {
            bottomSheetUseCase.onNewsClicked()
        }
    }

    private fun onBottomSheetShown() {
        viewModelScope.launch {
            newsRepository.recordBottomSheetShown()
        }
    }

// 예시 1
// 일반적인 코루틴은 아래처럼 사용할 수 있다.
//    fun fetchNews() {
//        newsRepository
//            .getNews()
//            .onEach {
//                _news.update { it }
//            }
//            .onCompletion {
//                // 성공하든 실패하든 도달하는 곳
//            }
//            .catch {
//                // 상위 스트림에서 발생한 에러 핸들링
//            }
//            .launchIn(viewModelScope)
//    }
}
