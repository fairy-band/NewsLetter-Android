package com.nexters.knownknowns.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.knownknowns.data.repository.AuthRepository
import com.nexters.knownknowns.data.repository.NewsRepository
import com.nexters.knownknowns.data.repository.RemoteConfigRepository
import com.nexters.knownknowns.data.repository.UserRepository
import com.nexters.knownknowns.domain.usecase.BottomSheetUseCase
import com.nexters.knownknowns.presentation.model.AuthInfo
import com.nexters.knownknowns.presentation.model.NewsFeed
import com.nexters.knownknowns.presentation.model.UserInfo
import com.nexters.knownknowns.presentation.model.toAuthInfo
import com.nexters.knownknowns.presentation.model.toNewsFeed
import com.nexters.knownknowns.presentation.model.toRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber
import java.util.UUID

@KoinViewModel
class HomeViewModel(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val bottomSheetUseCase: BottomSheetUseCase,
    remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<HomeSideEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    val news: StateFlow<ImmutableList<NewsFeed>> = newsRepository
        .getNews(userId = 14) // TODO: 실제 userId를 넣으세요.
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
            val userId = authRepository.getUserId().first()

            if (userId == null) registerUser()
            else loginUser()
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            val uuid = UUID.randomUUID().toString()

            authRepository.registerUser(
                AuthInfo(
                    deviceToken = uuid
                ).toRequest()
            ).onEach { response ->
                val id = response.toAuthInfo().id

                authRepository.setUserId(id)
            }.catch {
                Timber.e(it)
            }.launchIn(this)
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            val uuid = UUID.randomUUID().toString()

            authRepository.loginUser(
                deviceToken = uuid
            ).onEach { response ->
                val id = response.toAuthInfo().id

                authRepository.setUserId(id)
            }.catch {
                Timber.e(it)
            }.launchIn(this)
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
        preference: List<String>,
        workingExperience: String
    ) {
        viewModelScope.launch {
            userRepository.putUserInfo(
                UserInfo(
                    preference = preference[0],
                    workingExperience = workingExperience
                ).toRequest()
            ).onSuccess {

            }.onFailure(Timber::e)
        }

//        viewModelScope.launch {
//            userRepository.putUserInfo(
//                UserInfo(
//                    position = "FRONTEND",
//                    career = "STUDENT"
//                ).toRequest()
//            ).onCompletion {
//                Timber.tag("TAG").d("성공")
//            }.catch {
//                Timber.tag("TAG").d("실패: ${it.message}")
//            }
//        }
    }
}
