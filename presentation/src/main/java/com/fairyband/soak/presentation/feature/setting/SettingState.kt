package com.fairyband.soak.presentation.feature.setting

import com.fairyband.soak.data.model.response.UserInfoResponse
import com.fairyband.soak.presentation.feature.home.bottomsheet.Preference
import com.fairyband.soak.presentation.feature.home.bottomsheet.WorkingExperience

data class SettingState(
    val preferences: List<Preference> = emptyList(),
    val workingExperience: WorkingExperience? = null,
)

fun UserInfoResponse.toState(): SettingState =
    SettingState(
        preferences = preferences.mapNotNull(Preference::from),
        workingExperience = WorkingExperience.from(workingExperience),
    )
