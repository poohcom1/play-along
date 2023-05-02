package com.poohcom1.playalong.utils

import com.poohcom1.playalong.datatypes.Tempo
import org.junit.Assert.assertEquals
import org.junit.Test

class MetronomeTest {
  @Test
  fun tickTestExact() {
    val metronome = Metronome(Tempo.fromBpm(120))
    assertEquals(metronome.tick(0), true)
    assertEquals(metronome.tick(300), false)
    assertEquals(metronome.tick(500), true)
    assertEquals(metronome.tick(700), false)
    assertEquals(metronome.tick(1000), true)
  }

  @Test
  fun tickTestApprox() {
    val metronome = Metronome(Tempo.fromBpm(120))
    assertEquals(metronome.tick(0), true)
    assertEquals(metronome.tick(300), false)
    assertEquals(metronome.tick(600), true)
    assertEquals(metronome.tick(900), false)
    assertEquals(metronome.tick(1200), true)
  }
}
