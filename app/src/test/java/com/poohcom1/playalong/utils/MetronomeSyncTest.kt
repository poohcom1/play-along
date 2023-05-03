package com.poohcom1.playalong.utils

import com.poohcom1.playalong.datatypes.Tempo
import org.junit.Assert.assertEquals
import org.junit.Test

class MetronomeSyncTest {
  @Test
  fun exactSyncTest() {
    val metronomeSync = MetronomeSync(Tempo.fromBpm(120))

    metronomeSync.tick(0)
    assertEquals(false, metronomeSync.tick(250))
    assertEquals(true, metronomeSync.tick(500))
    assertEquals(false, metronomeSync.tick(750))
    assertEquals(true, metronomeSync.tick(1000))
    assertEquals(false, metronomeSync.tick(1250))
    assertEquals(true, metronomeSync.tick(1500))
  }

  @Test
  fun exactSyncOffsetTest() {
    val metronomeSync = MetronomeSync(Tempo.fromBpm(120).copy(msOffset = 250.0))

    metronomeSync.tick(0)
    assertEquals(true, metronomeSync.tick(250))
    assertEquals(false, metronomeSync.tick(500))
    assertEquals(true, metronomeSync.tick(750))
    assertEquals(false, metronomeSync.tick(1000))
    assertEquals(true, metronomeSync.tick(1250))
    assertEquals(false, metronomeSync.tick(1500))
  }
}
