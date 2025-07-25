package com.nexters.knownknowns.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.knownknowns.data.repository.NewsRepository
import com.nexters.knownknowns.presentation.model.NewsFeed
import com.nexters.knownknowns.presentation.model.toNewsFeed
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(newsRepository: NewsRepository) : ViewModel() {
    val news: StateFlow<List<NewsFeed>> = newsRepository
        .getNews()
        .map { it.map { response -> response.toNewsFeed() } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

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