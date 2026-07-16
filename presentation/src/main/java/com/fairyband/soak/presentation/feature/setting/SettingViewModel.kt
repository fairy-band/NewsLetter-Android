package com.fairyband.soak.presentation.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyband.soak.data.repository.UserRepository
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import com.fairyband.soak.presentation.feature.home.bottomsheet.WorkingExperience
import com.fairyband.soak.presentation.model.UserInfo
import com.fairyband.soak.presentation.model.toRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class SettingViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    private val _state = MutableStateFlow(SettingState())
    val state: StateFlow<SettingState> = _state.asStateFlow()

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

    fun saveUserInfo(
        preferences: List<String>,
        workingExperience: String
    ) {
        userRepository.putUserInfo(
            UserInfo(
                preferences = preferences,
                workingExperience = workingExperience
            ).toRequest()
        ).onEach {
            _state.update { state ->
                state.copy(
                    preferences = preferences.mapNotNull(Preference::from),
                    workingExperience = WorkingExperience.from(workingExperience),
                )
            }
        }.catch {
            Timber.e(it)
        }.launchIn(viewModelScope)
    }
}
