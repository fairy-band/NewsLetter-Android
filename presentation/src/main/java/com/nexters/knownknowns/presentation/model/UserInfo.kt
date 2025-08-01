package com.nexters.knownknowns.presentation.model

import com.nexters.knownknowns.data.model.request.UserInfoRequest

data class UserInfo(
    val preference: String,
    val workingExperience: String,
)

fun UserInfo.toRequest(): UserInfoRequest =
    UserInfoRequest(
        preference = preference,
        workingExperience = workingExperience
    )
