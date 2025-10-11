package com.fairyband.soak.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.model.abtest.HomeTitleVariant
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.data.repository.RemoteConfigRepository
import com.fairyband.soak.data.repository.UserRepository
import com.fairyband.soak.domain.usecase.BottomSheetUseCase
import com.fairyband.soak.domain.usecase.PutUserInfoUseCase
import com.fairyband.soak.presentation.model.NewsFeed
import com.fairyband.soak.presentation.model.UserInfo
import com.fairyband.soak.presentation.model.toNewsFeed
import com.fairyband.soak.presentation.model.toRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    newsRepository: NewsRepository,
    private val userRepository: UserRepository,
    private val bottomSheetUseCase: BottomSheetUseCase,
    private val putUserInfoUseCase: PutUserInfoUseCase,
    remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeSideEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _cardShown = MutableStateFlow(false)
    private val cardShown: StateFlow<Boolean> = _cardShown.asStateFlow()
    private val notificationEnabled = userRepository.notificationEnabled
    val shouldShowNotificationSetting: StateFlow<Boolean> = userRepository
        .streak
        .combine(cardShown) { streak, cardShown ->
            // 방금 캐로셀을 닫았거나, 방문 일수가 2일 이상이면 바텀 시트를 노출한다.
            streak >= 2 || cardShown
        }
        .combine(notificationEnabled) { shouldShow, enabled ->
            // 하지만 알림 세팅이 비활성화 되어 있다면 노출하지 않는다.
            shouldShow && enabled
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    val news: StateFlow<ImmutableList<NewsFeed>> = newsRepository
        .news
        .map {
            it.map { response ->
                response.toNewsFeed()
            }.toImmutableList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            persistentListOf()
        )

    val cardColorType: StateFlow<String> = remoteConfigRepository
        .getCardColorType()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            "B",
        )

    val homeTitleVariant: StateFlow<HomeTitleVariant> = remoteConfigRepository
        .getHomeTitleVariant()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeTitleVariant.EXISTING,
        )

    init {
        visitApp()
        observeBottomSheetTrigger()
    }

    private fun observeBottomSheetTrigger() {
        viewModelScope.launch {
            bottomSheetUseCase.shouldShowBottomSheet
                .filter { it }
                .collect {
                    _eventFlow.emit(HomeSideEffect.ShowBottomSheet)
                    onBottomSheetShown()
                }
        }
    }

    private fun onBottomSheetShown() {
        viewModelScope.launch {
            userRepository.recordBottomSheetShown()
        }
    }

    fun saveUserInfo(
        preferences: List<String>,
        workingExperience: String
    ) = viewModelScope.launch {
        putUserInfoUseCase(
            UserInfo(
                preferences = preferences,
                workingExperience = workingExperience
            ).toRequest()
        )
    }

    fun disableNotificationSetting() = viewModelScope.launch {
        userRepository.disableNotificationSetting()
    }

    fun onCardShown() {
        _cardShown.update { true }
    }

    private fun visitApp() = viewModelScope.launch {
        userRepository.visitApp()
    }
}
