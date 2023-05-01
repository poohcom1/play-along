package com.poohcom1.playalong.utils

import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.exoplayer2.ExoPlayer

class TempoTapCalculator(private val maxDelayMs: Long, private val roundTempo: Boolean) {
    private val beatDelaysMs = ArrayList<Long>()

    private var bpm by mutableStateOf(120.0)
    private var lastTimeStamp = 0L

    var player: ExoPlayer? = null

    fun tap(): Double {
        if (player != null) {
            Log.d("TempoTapCalculator", "Current position: ${player?.currentPosition ?: 0}")
        }

        val currentTime = SystemClock.elapsedRealtime()
        val delay = currentTime - lastTimeStamp
        lastTimeStamp = currentTime

        if (delay < maxDelayMs) {
            beatDelaysMs.add(delay)
        } else {
            beatDelaysMs.clear()
        }

        bpm = calculateTempo(beatDelaysMs)

        return if (roundTempo) {
            bpm.toInt().toDouble()
        } else {
            bpm
        }
    }
}

fun calculateTempo(beatDelayMs: List<Long>, defaultBpm: Double = 120.0): Double {
    if (beatDelayMs.size < 2) {
        return defaultBpm
    }

    return 60f * 1000f / beatDelayMs.average()
}