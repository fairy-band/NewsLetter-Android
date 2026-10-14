package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NewsLetterDataSource
import com.fairyband.soak.data.model.request.BookmarkRequest
import com.fairyband.soak.data.model.response.BookmarksResponse
import com.fairyband.soak.data.repository.BookmarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class BookmarkRepositoryImpl(
    private val newsLetterDataSource: NewsLetterDataSource,
    private val authDataSource: AuthDataSource,
) : BookmarkRepository {
    private val _bookmarkedIds = MutableStateFlow<Set<Long>>(emptySet())
    override val bookmarkedIds: StateFlow<Set<Long>> = _bookmarkedIds.asStateFlow()

    override suspend fun syncBookmarkedIds() {
        val userId = authDataSource.getUserId()
        val ids = mutableSetOf<Long>()
        var page = 0
        do {
            val response = newsLetterDataSource.getBookmarks(userId = userId, page = page, size = SYNC_PAGE_SIZE)
            ids += response.bookmarks.map { it.exposureContentId }
            page++
        } while (response.hasMore && page < MAX_SYNC_PAGE)
        _bookmarkedIds.value = ids
    }

    override suspend fun getBookmarks(page: Int): BookmarksResponse {
        val userId = authDataSource.getUserId()
        val response = newsLetterDataSource.getBookmarks(userId = userId, page = page, size = PAGE_SIZE)
        _bookmarkedIds.update { it + response.bookmarks.map { bookmark -> bookmark.exposureContentId } }
        return response
    }

    override suspend fun updateBookmark(exposureContentId: Long, isBookmarked: Boolean) {
        val previous = _bookmarkedIds.value
        _bookmarkedIds.update { if (isBookmarked) it + exposureContentId else it - exposureContentId }

        runCatching {
            newsLetterDataSource.updateBookmark(
                BookmarkRequest(
                    userId = authDataSource.getUserId(),
                    exposureContentId = exposureContentId,
                    isBookmarked = isBookmarked,
                )
            )
        }.onFailure { throwable ->
            _bookmarkedIds.value = previous
            throw throwable
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val SYNC_PAGE_SIZE = 100
        private const val MAX_SYNC_PAGE = 10
    }
}
