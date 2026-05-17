package com.jikana.app.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object HapticFeedback {

    fun correct(context: Context) {
        vibrate(context, longArrayOf(0, 50), -1)
    }

    fun wrong(context: Context) {
        vibrate(context, longArrayOf(0, 80, 60, 80), -1)
    }

    fun tap(context: Context) {
        vibrate(context, longArrayOf(0, 20), -1)
    }

    private fun vibrate(context: Context, pattern: LongArray, repeat: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(
                    Context.VIBRATOR_MANAGER_SERVICE
                ) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(
                    VibrationEffect.createWaveform(pattern, repeat)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, repeat)
            }
        } catch (e: Exception) { }
    }
}