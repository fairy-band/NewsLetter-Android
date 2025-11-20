package com.fairyband.soak.presentation.feature.home.dialog

import com.fairyband.soak.presentation.R

enum class Language(
    val key: String,
    val label: Int,
) {
    KOREAN("KOREAN", R.string.home_popup_language_korean),
    ENGLISH("ENGLISH", R.string.home_popup_language_english),
    UNKNOWN("UNKNOWN", R.string.home_popup_language_unknown);

    companion object {
        fun fromKey(key: String): Language = entries.find { it.key == key } ?: UNKNOWN
    }
}
