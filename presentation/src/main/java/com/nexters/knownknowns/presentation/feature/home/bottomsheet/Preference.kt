package com.nexters.knownknowns.presentation.feature.home.bottomsheet

import com.nexters.knownknowns.presentation.R

enum class Preference(
    val label: String,
    val icon: Int
) {
    ANDROID(
        label = "AND",
        icon = R.drawable.ic_home_android
    ),
    IOS(
        label = "iOS",
        icon = R.drawable.ic_home_ios
    ),
    FRONTEND(
        label = "FE",
        icon = R.drawable.ic_home_frontend
    ),
    BACKEND(
        label = "BE",
        icon = R.drawable.ic_home_backend
    )
}
