package com.nexters.knownknowns.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.knownknowns.data.model.NewsResponse
import com.nexters.knownknowns.data.repository.NewsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(newsRepository: NewsRepository) : ViewModel() {
//      예시 1
//     private val _news = MutableStateFlow<List<NewsResponse>>(emptyList())
//      val news = _news.asStateFlow()

    val news: StateFlow<List<NewsResponse>> = newsRepository.getNews().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    // 예시 1
    // 이렇게 받을 수도 있지만 더 진화된 방법을 쓸 수 있음.
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