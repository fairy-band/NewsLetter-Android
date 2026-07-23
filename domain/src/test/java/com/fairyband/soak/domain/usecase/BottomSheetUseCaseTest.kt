package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.model.request.UserInfoRequest
import com.fairyband.soak.data.model.response.UserInfoResponse
import com.fairyband.soak.data.repository.UserRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private class FakeUserRepository(
    streakValue: Int,
    private val userInfoProvider: () -> UserInfoResponse,
) : UserRepository {
    override val streak: Flow<Int> = flowOf(streakValue)
    override val notificationEnabled: Flow<Boolean> = flowOf(false)
    override fun getUserInfo(): Flow<UserInfoResponse> = flow { emit(userInfoProvider()) }
    override fun putUserInfo(request: UserInfoRequest): Flow<Unit> = flowOf(Unit)
    override suspend fun disableNotificationSetting() = Unit
    override suspend fun visitApp() = Unit
}

private fun userInfo(isOnboarded: Boolean) = UserInfoResponse(
    id = 1L,
    isOnboarded = isOnboarded,
)

private fun useCase(streak: Int, userInfoProvider: () -> UserInfoResponse) =
    BottomSheetUseCase(FakeUserRepository(streak, userInfoProvider))

class BottomSheetUseCaseTest : StringSpec({
    "방문 2일 미만이면 노출하지 않는다" {
        useCase(streak = 1) { userInfo(isOnboarded = true) }
            .shouldShowBottomSheet.first() shouldBe false
    }

    "방문 2일 이상이어도 isOnboarded 가 false 면 노출하지 않는다" {
        useCase(streak = 2) { userInfo(isOnboarded = false) }
            .shouldShowBottomSheet.first() shouldBe false
    }

    "방문 2일 이상이고 isOnboarded 가 true 면 노출한다" {
        useCase(streak = 2) { userInfo(isOnboarded = true) }
            .shouldShowBottomSheet.first() shouldBe true
    }

    "사용자 정보 조회에 실패하면 노출하지 않는다" {
        useCase(streak = 2) { throw RuntimeException("network error") }
            .shouldShowBottomSheet.first() shouldBe false
    }
})
