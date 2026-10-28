package com.fairyband.soak.presentation.feature.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.BookmarkRepository
import com.fairyband.soak.data.repository.SharedContentRepository
import com.fairyband.soak.presentation.model.BookmarkFeed
import com.fairyband.soak.presentation.model.toBookmarkFeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository,
    sharedContentRepository: SharedContentRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(BookmarkState())
    val state = _state.asStateFlow()

    private var nextPage = 0
    private var hasMore = true
    private var loadingJob: Job? = null

    init {
        sharedContentRepository.sharedContents
            .onEach { contents ->
                _state.update { it.copy(sharedFeeds = contents.map { content -> content.toBookmarkFeed() }) }
            }
            .launchIn(viewModelScope)

        bookmarkRepository.bookmarkedIds
            .onEach { ids -> _state.update { it.copy(bookmarkedIds = ids) } }
            .launchIn(viewModelScope)
    }

    fun selectTab(tab: BookmarkTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    /**
     * 다른 화면에서 북마크가 바뀌었을 수 있어서, 보관함 탭에 들어올 때마다 처음부터 다시 조회해요.
     */
    fun refresh() {
        loadingJob?.cancel()
        loadingJob = null
        nextPage = 0
        hasMore = true
        loadBookmarks(isRefresh = true)
    }

    fun loadMore() {
        loadBookmarks(isRefresh = false)
    }

    private fun loadBookmarks(isRefresh: Boolean) {
        if (loadingJob != null || !hasMore) return

        val job = viewModelScope.launch {
            runCatching { bookmarkRepository.getBookmarks(page = nextPage) }
                .onSuccess { response ->
                    nextPage = response.page + 1
                    hasMore = response.hasMore
                    val newFeeds = response.bookmarks.map { it.toBookmarkFeed() }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            totalCount = response.totalCount,
                            feeds = if (isRefresh) newFeeds else it.feeds + newFeeds,
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "보관함 목록 조회 실패")
                    _state.update { it.copy(isLoading = false) }
                }
        }
        loadingJob = job

        // 취소된 이전 job 의 완료 콜백이 새 job 의 참조를 지우지 않도록 확인해요.
        job.invokeOnCompletion {
            if (loadingJob === job) loadingJob = null
        }
    }

    /**
     * 목록에서 먼저 지우고, 요청이 실패하면 원래 자리로 되돌려요.
     */
    fun removeBookmark(feed: BookmarkFeed) {
        val previous = _state.value
        _state.update {
            it.copy(
                feeds = it.feeds - feed,
                totalCount = (it.totalCount - 1).coerceAtLeast(0),
            )
        }

        viewModelScope.launch {
            runCatching {
                bookmarkRepository.updateBookmark(
                    exposureContentId = feed.exposureContentId,
                    isBookmarked = false,
                )
            }.onFailure { throwable ->
                Timber.e(throwable, "북마크 해제 실패")
                _state.update { it.copy(feeds = previous.feeds, totalCount = previous.totalCount) }
            }
        }
    }

    /**
     * 공유받은 목록은 북마크 여부와 상관없이 유지되고, 아이콘 상태만 바뀌어요.
     */
    fun toggleSharedBookmark(feed: BookmarkFeed) {
        val isBookmarked = feed.exposureContentId !in _state.value.bookmarkedIds
        viewModelScope.launch {
            runCatching {
                bookmarkRepository.updateBookmark(
                    exposureContentId = feed.exposureContentId,
                    isBookmarked = isBookmarked,
                )
            }.onFailure { throwable ->
                Timber.e(throwable, "공유받은 콘텐츠 북마크 변경 실패")
            }
        }
    }
}
