package com.fairyband.soak.core.extension

import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toPattern(pattern: String): String? {
    return try {
        DateTimeFormatter.ofPattern(pattern).format(this)
    } catch (e: Exception) {
        Timber.d("도대체 뭘 넣은 거야?? LocalDate(${this}) 변환(${pattern})하는데 에러 났잖아! $e")

        null
    }
}