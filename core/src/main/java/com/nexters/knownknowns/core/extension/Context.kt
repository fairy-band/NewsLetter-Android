package com.nexters.knownknowns.core.extension

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager

val Context.vibrator: Vibrator
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }