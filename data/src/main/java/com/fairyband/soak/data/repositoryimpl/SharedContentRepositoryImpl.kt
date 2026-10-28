package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.datasource.AuthDataSource
import com.fairyband.soak.data.datasource.NewsLetterDataSource
import com.fairyband.soak.data.model.request.SharedContentRequest
import com.fairyband.soak.data.model.response.SharedContentsResponse
import com.fairyband.soak.data.repository.SharedContentRepository
import org.koin.core.annotation.Single

@Single
class SharedContentRepositoryImpl(
    private val newsLetterDataSource: NewsLetterDataSource,
    private val authDataSource: AuthDataSource,
) : SharedContentRepository {
    override suspend fun getSharedContents(page: Int): SharedContentsResponse {
        return newsLetterDataSource.getSharedContents(
            userId = authDataSource.getUserId(),
            page = page,
            size = PAGE_SIZE,
        )
    }

    override suspend fun addSharedContent(exposureContentId: Long) {
        newsLetterDataSource.updateSharedContent(
            SharedContentRequest(
                userId = authDataSource.getUserId(),
                exposureContentId = exposureContentId,
                isShared = true,
            )
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
