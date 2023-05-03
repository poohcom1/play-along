package com.poohcom1.playalong.datatypes

data class Tempo(val msPerBeat: Double = 500.0, val msOffset: Double = 0.0) {
  companion object {
    fun fromBpm(bpm: Long): Tempo {
      return Tempo(60000.0 / bpm)
    }
  }
}
