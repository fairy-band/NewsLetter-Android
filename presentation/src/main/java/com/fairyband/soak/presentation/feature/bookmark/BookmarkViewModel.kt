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
    private val sharedContentRepository: SharedContentRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(BookmarkState())
    val state = _state.asStateFlow()

    private var nextPage = 0
    private var hasMore = true
    private var loadingJob: Job? = null

    private var sharedNextPage = 0
    private var sharedHasMore = true
    private var sharedLoadingJob: Job? = null

    init {
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
        refreshShared()
    }

    fun loadMore() {
        loadBookmarks(isRefresh = false)
    }

    fun loadMoreShared() {
        loadSharedContents(isRefresh = false)
    }

    /**
     * 카카오톡 공유 링크로 받은 콘텐츠를 등록하고, 등록이 끝난 뒤 목록을 다시 조회해야 새 항목이 보여요.
     */
    fun receiveSharedContent(exposureContentId: Long) {
        viewModelScope.launch {
            runCatching { sharedContentRepository.addSharedContent(exposureContentId) }
                .onFailure { throwable -> Timber.e(throwable, "공유받은 콘텐츠 등록 실패") }
            refreshShared()
        }
    }

    private fun refreshShared() {
        sharedLoadingJob?.cancel()
        sharedLoadingJob = null
        sharedNextPage = 0
        sharedHasMore = true
        loadSharedContents(isRefresh = true)
    }

    private fun loadSharedContents(isRefresh: Boolean) {
        if (sharedLoadingJob != null || !sharedHasMore) return

        val job = viewModelScope.launch {
            runCatching { sharedContentRepository.getSharedContents(page = sharedNextPage) }
                .onSuccess { response ->
                    sharedNextPage = response.page + 1
                    sharedHasMore = response.hasMore
                    val newFeeds = response.sharedContents.map { it.toBookmarkFeed() }
                    _state.update {
                        it.copy(
                            isSharedLoading = false,
                            sharedTotalCount = response.totalCount,
                            sharedFeeds = if (isRefresh) newFeeds else it.sharedFeeds + newFeeds,
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "공유받은 목록 조회 실패")
                    _state.update { it.copy(isSharedLoading = false) }
                }
        }
        sharedLoadingJob = job

        // 취소된 이전 job 의 완료 콜백이 새 job 의 참조를 지우지 않도록 확인해요.
        job.invokeOnCompletion {
            if (sharedLoadingJob === job) sharedLoadingJob = null
        }
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
