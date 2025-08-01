package com.nexters.knownknowns.presentation.feature.home.bottomsheet

enum class WorkingExperience(
    val label: String,
    val stringValue: String,
) {
    STUDENT(
        label = "대학생 · 취준생",
        stringValue = "STUDENT"
    ),
    YEARS_1_TO_3(
        label = "1 ~ 3년차",
        stringValue = "JUNIOR"
    ),
    YEARS_4_TO_7(
        label = "4 ~ 7년차",
        stringValue = "MID"
    ),
    YEARS_8_TO_10(
        label = "8 ~ 10년차",
        stringValue = "SENIOR"
    ),
    OVER_10_YEARS(
        label = "10년차 이상",
        stringValue = "EXPERT"
    )
}
