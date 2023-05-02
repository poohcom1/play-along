package com.poohcom1.playalong.utils

import android.os.SystemClock
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.interfaces.Clock
import kotlin.math.max

class TempoTapCalculator(
    private val clock: Clock =
        object : Clock {
          override fun currentTimeMs(): Long {
            return SystemClock.elapsedRealtime()
          }
        },
    private val maxDelayMs: Long = 2000
) {
  var msPerBeat = 500L
    private set
  var msOffset = 0L
    private set

  val tempo = Tempo(msPerBeat.toDouble(), msOffset.toDouble())

  private val beatDelaysMs = ArrayList<Long>()
  private var lastTimeStamp = 0L

  fun tap(offsetMs: Long) {
    val currentTime = clock.currentTimeMs()
    val delay = currentTime - lastTimeStamp

    if (delay < maxDelayMs) {
      if (lastTimeStamp != 0L) beatDelaysMs.add(delay)
    } else {
      beatDelaysMs.clear()
    }

    lastTimeStamp = currentTime

    val avgMsPerBeat = beatDelaysMs.average()

    msOffset = (avgMsPerBeat - (offsetMs % avgMsPerBeat)).toLong()
    msPerBeat = max(avgMsPerBeat.toLong(), 250L)
  }
}
