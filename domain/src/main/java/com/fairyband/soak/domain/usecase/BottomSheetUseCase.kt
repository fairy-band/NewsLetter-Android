package com.fairyband.soak.domain.usecase

import com.fairyband.soak.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class BottomSheetUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * 맞춤 설정 바텀 시트 노출 여부를 판단해요.
     *
     * 앱 첫 실행이 아닌, 둘째 날 이후 첫 실행 시(방문 일수 2일 이상)에 노출해요.
     * 로컬 방문 데이터가 초기화될 수 있으므로, 서버의 [isOnboarded][com.fairyband.soak.data.model.response.UserInfoResponse.isOnboarded]가
     * true(= 아직 맞춤 설정을 하지 않은 사용자)일 때에만 노출해요.
     * 사용자 정보 조회에 실패하면 노출하지 않아요.
     */
    val shouldShowBottomSheet: Flow<Boolean> = combine(
        userRepository.streak,
        userRepository.getUserInfo()
            .map { it.isOnboarded }
            .catch { emit(false) },
    ) { streak, isOnboarded ->
        streak >= VISIT_THRESHOLD_DAYS && isOnboarded
    }.distinctUntilChanged()

    companion object {
        private const val VISIT_THRESHOLD_DAYS = 2
    }
}
