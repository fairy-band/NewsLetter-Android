package com.fairyband.soak.data.repository

import com.fairyband.soak.data.local.user.BottomSheetState
import com.fairyband.soak.data.model.request.UserInfoRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val bottomSheetFlow: Flow<BottomSheetState>
    suspend fun resetState()
    val streak: Flow<Int>
    val notificationEnabled: Flow<Boolean>
    suspend fun recordBottomSheetShown()
    fun putUserInfo(request: UserInfoRequest): Flow<Unit>

    /**
     * 만약 이미 알림 설정 바텀시트를 노출했다면 3일간 노출하지 않아요.
     * 내부적으로는 마지막으로 바텀시트를 보여준 날짜를 기록해요.
     */
    suspend fun disableNotificationSetting()

    /**
     * 2일 연속 방문 시 알림 설정하는 바텀시트를 띄워줘요.
     * 이 메서드는 로컬에 방문 정보를 저장하기 위해 호출해요.
     * @see streak visitApp() 메서드를 호출하면 이 Flow가 업데이트 돼요.
     */
    suspend fun visitApp()
}
