package com.fairyband.soak.data.repositoryimpl

import com.fairyband.soak.data.local.shared.SharedContentDataStore
import com.fairyband.soak.data.model.local.SharedContent
import com.fairyband.soak.data.repository.SharedContentRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class SharedContentRepositoryImpl(
    private val sharedContentDataStore: SharedContentDataStore,
) : SharedContentRepository {
    override val sharedContents: Flow<List<SharedContent>> = sharedContentDataStore.sharedContents

    override suspend fun saveSharedContent(content: SharedContent) {
        sharedContentDataStore.save(content)
    }
}
