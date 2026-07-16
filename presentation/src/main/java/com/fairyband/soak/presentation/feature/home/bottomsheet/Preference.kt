package com.fairyband.soak.presentation.feature.home.bottomsheet

import com.fairyband.soak.presentation.R

enum class Preference(
    val label: String,
    val icon: Int,
    val stringValue: String,
    val categoryId: String,
) {
    ANDROID(
        label = "AND",
        icon = R.drawable.ic_home_android,
        stringValue = "ANDROID",
        categoryId = "4",
    ),
    IOS(
        label = "iOS",
        icon = R.drawable.ic_home_ios,
        stringValue = "IOS",
        categoryId = "3",
    ),
    FRONTEND(
        label = "FE",
        icon = R.drawable.ic_home_frontend,
        stringValue = "FRONTEND",
        categoryId = "2",
    ),
    BACKEND(
        label = "BE",
        icon = R.drawable.ic_home_backend,
        stringValue = "BACKEND",
        categoryId = "1",
    ),
    DEVOPS(
        label = "DEVOPS",
        icon = R.drawable.ic_home_devops,
        stringValue = "DEVOPS",
        categoryId = "5",
    );

    companion object {
        /**
         * 서버가 모르는 직군을 내려주더라도 크래시하지 않도록 null 을 반환해요.
         */
        fun from(stringValue: String?): Preference? =
            entries.find { it.stringValue == stringValue }
    }
}
