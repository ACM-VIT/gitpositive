package org.acmvit.gitpositive.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity

fun getColorStr(text: String, color: String): String = "<font color=$color>$text</font>"

fun Context.doVibration() {
    val vibrator = this.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(
        VibrationEffect.createOneShot(
            50,
            VibrationEffect.EFFECT_TICK
        )
    )
}