package com.fairyband.soak.presentation.model

import com.fairyband.soak.data.model.request.UserInfoRequest

data class UserInfo(
    val preferences: List<String>,
    val workingExperience: String,
)

fun UserInfo.toRequest(): UserInfoRequest =
    UserInfoRequest(
        preferences = preferences,
        workingExperience = workingExperience
    )
