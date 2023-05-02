package com.poohcom1.playalong.utils

fun msPerBeatToBpm(msPerBeat: Double): Double {
  return 60.0 / (msPerBeat / 1000.0)
}

fun bpmToMsPerBeat(bpm: Double): Double {
  return 60.0 / bpm * 1000.0
}

fun calculateTempo(beatDelayMs: List<Long>, defaultBpm: Double = 120.0): Double {
  if (beatDelayMs.size < 2) {
    return defaultBpm
  }

  return 60f * 1000f / beatDelayMs.average()
}
