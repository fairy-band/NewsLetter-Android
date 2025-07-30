package com.nexters.knownknowns.data.repositoryimpl

import com.nexters.knownknowns.core.local.DataStore
import com.nexters.knownknowns.data.model.NewsResponse
import com.nexters.knownknowns.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
internal class NewsRepositoryImpl(
    private val dataStore: DataStore
) : NewsRepository {
    private val _news = listOf(
        NewsResponse(
            id = "1",
            title = " 도망 간 첫 번째 슬픈 이야기. 여름이었다.",
            keyword = "Kotlin",
            letter = "Android Weekly",
            summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
            url = "https://www.naver.com"
        ),
        NewsResponse(
            id = "2",
            title = " 도망 간 두 번째 슬픈 이야기. 여름이었다.",
            keyword = "Kotlin",
            letter = "Android Weekly",
            summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
            url = "https://www.naver.com"
        ),
        NewsResponse(
            id = "3",
            title = " 도망 간 세 번째 슬픈 이야기. 여름이었다.",
            keyword = "Kotlin",
            letter = "Android Weekly",
            summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.",
            url = "https://www.naver.com"
        ),
        NewsResponse(
            id = "4",
            title = "도망가지 않은 네 번째 기쁜 이야기. 겨울이었다.",
            keyword = "Kotlin",
            letter = "Android Weekly",
            summary = "Anatolii Frolov",
            url = "https://www.naver.com"
        ),
        NewsResponse(
            id = "5",
            title = " 도망 간 다섯 번째 슬픈 이야기. 여름이었다.",
            keyword = "Kotlin",
            letter = "Android Weekly",
            summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
            url = "https://www.naver.com"
        ),
        NewsResponse(
            id = "6",
            title = " 도망 간 마지막 슬픈 이야기.",
            keyword = "Kotlin",
            letter = "Android Weekly",
            summary = "Anatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역직렬화할 때 실제 싱글톤 동작을 유지하려면 사용자 정의 어댑터가 필요하다고 강조합니다.\nAnatolii Frolov는 Kotlin 객체 싱글톤이 Gson과 같은 라이브러리에 의해 복제될 수 있으므로 역렬화할 때 실제 싱글톤 동작을 강조합니다.",
            url = "https://www.naver.com"
        ),
    )

    override fun getNews(): Flow<List<NewsResponse>> = flow {
        emit(_news.toList())
    }

    override suspend fun getClickCount(): Flow<Int> = dataStore.clickCountFlow

    override suspend fun incrementClickCount() {
        dataStore.incrementClickCount()
    }
}
