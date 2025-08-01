package com.nexters.knownknowns.presentation.feature.home.bottomsheet

import com.nexters.knownknowns.presentation.R

enum class Preference(
    val label: String,
    val icon: Int,
    val stringValue: String,
) {
    ANDROID(
        label = "AND",
        icon = R.drawable.ic_home_android,
        stringValue = "ANDROID"
    ),
    IOS(
        label = "iOS",
        icon = R.drawable.ic_home_ios,
        stringValue = "IOS"
    ),
    FRONTEND(
        label = "FE",
        icon = R.drawable.ic_home_frontend,
        stringValue = "FRONTEND"
    ),
    BACKEND(
        label = "BE",
        icon = R.drawable.ic_home_backend,
        stringValue = "BACKEND"
    )
}
