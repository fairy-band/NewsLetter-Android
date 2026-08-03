package com.fairyband.soak.presentation.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.UserRepository
import com.fairyband.soak.domain.usecase.PutUserInfoUseCase
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import com.fairyband.soak.presentation.feature.home.bottomsheet.WorkingExperience
import com.fairyband.soak.presentation.model.UserInfo
import com.fairyband.soak.presentation.model.toRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class SettingViewModel(
    private val userRepository: UserRepository,
    private val putUserInfoUseCase: PutUserInfoUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(SettingState())
    val state: StateFlow<SettingState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SettingSideEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadUserInfo()
    }

    /**
     * 조회에 실패하면 아무것도 선택되지 않은 상태로 바텀시트를 열어요.
     */
    private fun loadUserInfo() {
        userRepository.getUserInfo().onEach { response ->
            _state.value = response.toState()
        }.catch {
            Timber.e(it)
        }.launchIn(viewModelScope)
    }

    /**
     * 저장에 성공해야만 상태를 갱신해요. CTA 활성 여부를 이 상태로 판단하기 때문에,
     * 실패했는데 갱신하면 "변경 없음"으로 오판해 재시도가 막혀요.
     */
    fun saveUserInfo(
        preferences: List<String>,
        workingExperience: String
    ) {
        viewModelScope.launch {
            runCatching {
                putUserInfoUseCase(
                    UserInfo(
                        preferences = preferences,
                        workingExperience = workingExperience
                    ).toRequest()
                )
            }.onSuccess {
                _state.update { state ->
                    state.copy(
                        preferences = preferences.mapNotNull(Preference::from),
                        workingExperience = WorkingExperience.from(workingExperience),
                    )
                }
                _eventFlow.emit(SettingSideEffect.UserInfoChanged)
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}
