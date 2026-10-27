package com.fairyband.soak.presentation.deeplink

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single

/**
 * 카카오톡 공유 링크로 들어왔을 때 보관함 '공유받은' 탭을 열어 달라는 요청을 전달해요.
 *
 * 링크는 스플래시 화면보다 먼저 처리되므로, 이벤트가 아니라 상태로 들고 있다가
 * 탭 화면이 준비되면 [consume] 해요.
 */
@Single
class SharedLinkNavigator {
    private val _openSharedTabRequested = MutableStateFlow(false)
    val openSharedTabRequested: StateFlow<Boolean> = _openSharedTabRequested.asStateFlow()

    fun requestOpenSharedTab() {
        _openSharedTabRequested.value = true
    }

    fun consume() {
        _openSharedTabRequested.value = false
    }
}
