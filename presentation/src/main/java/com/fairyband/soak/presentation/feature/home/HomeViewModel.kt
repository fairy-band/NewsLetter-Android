package com.fairyband.soak.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.NewsRepository
import com.fairyband.soak.data.repository.RemoteConfigRepository
import com.fairyband.soak.data.repository.UserRepository
import com.fairyband.soak.domain.usecase.BottomSheetUseCase
import com.fairyband.soak.domain.usecase.RegisterOrLoginUseCase
import com.fairyband.soak.presentation.model.NewsFeed
import com.fairyband.soak.presentation.model.UserInfo
import com.fairyband.soak.presentation.model.toNewsFeed
import com.fairyband.soak.presentation.model.toRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class HomeViewModel(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository,
    private val registerOrLoginUseCase: RegisterOrLoginUseCase,
    private val bottomSheetUseCase: BottomSheetUseCase,
    remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeSideEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    val news: StateFlow<ImmutableList<NewsFeed>> = newsRepository
        .getNews()
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

    init {
        observeBottomSheetTrigger()
        registerOrLogin()
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

    private fun registerOrLogin() {
        viewModelScope.launch {
            registerOrLoginUseCase()
                .onFailure(Timber::e)
        }
    }

    fun onNewsClicked() {
        viewModelScope.launch {
            bottomSheetUseCase.onNewsClicked()
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
    ) {
        userRepository.putUserInfo(
            UserInfo(
                preferences = preferences,
                workingExperience = workingExperience
            ).toRequest()
        ).catch {
            Timber.e(it.message)
        }.launchIn(viewModelScope)
    }
}
