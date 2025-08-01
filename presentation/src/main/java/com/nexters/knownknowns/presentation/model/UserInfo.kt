package com.nexters.knownknowns.presentation.model

import com.nexters.knownknowns.data.model.request.UserInfoRequest

data class UserInfo(
    val position: String,
    val career: String,
)

fun UserInfo.toRequest(): UserInfoRequest =
    UserInfoRequest(
        preference = position,
        workingExperience = career
    )
