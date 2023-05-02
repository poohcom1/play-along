package com.poohcom1.playalong.utils

import com.poohcom1.playalong.datatypes.Tempo

class Metronome(private val tempo: Tempo) {
  private var previousMod: Double = 0.0

  fun tick(timeMs: Long): Boolean {
    if (timeMs == 0L) {
      return false
    }

    val mod = (timeMs + tempo.msOffset) % tempo.msPerBeat

    val shouldTick = mod < previousMod || mod == 0.0

    previousMod = mod

    return shouldTick
  }
}
