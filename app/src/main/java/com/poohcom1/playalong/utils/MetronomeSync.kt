package com.poohcom1.playalong.utils

import com.poohcom1.playalong.datatypes.Tempo

class MetronomeSync(private val tempo: Tempo) {
  private var previousMod: Double = 0.0

  fun tick(songPositionMs: Long): Boolean {
    val mod = (songPositionMs - tempo.msOffset) % tempo.msPerBeat

    val onBeat = mod < previousMod || mod == 0.0

    previousMod = mod

    return onBeat
  }
}
