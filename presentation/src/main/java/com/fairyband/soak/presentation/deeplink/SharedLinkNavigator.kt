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
    private val _openSharedTabRequest = MutableStateFlow<OpenSharedTabRequest?>(null)
    val openSharedTabRequest: StateFlow<OpenSharedTabRequest?> = _openSharedTabRequest.asStateFlow()

    /**
     * @param exposureContentId 공유받은 목록에 등록할 콘텐츠예요. 링크에서 읽지 못했으면 null 이고, 탭만 열어요.
     */
    fun requestOpenSharedTab(exposureContentId: Long?) {
        _openSharedTabRequest.value = OpenSharedTabRequest(exposureContentId)
    }

    fun consume() {
        _openSharedTabRequest.value = null
    }
}

data class OpenSharedTabRequest(val exposureContentId: Long?)
