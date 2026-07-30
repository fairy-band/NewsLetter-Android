package com.fairyband.soak.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val id: Long,
    val preferences: List<String> = emptyList(),
    val workingExperience: String? = null,
    val isOnboarded: Boolean = false, // true일 경우 온보딩(맞춤 설정)을 해야 하는 사용자라는 의미
)
