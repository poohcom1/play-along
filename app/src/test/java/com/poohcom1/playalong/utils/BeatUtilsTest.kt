package com.poohcom1.playalong.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class BeatUtilsTest {
  @Test
  fun calculateTempoTest() {
    assertEquals(calculateTempo(listOf(1000, 1000, 1000, 1000)), 60.0, 0.01)
    assertEquals(calculateTempo(listOf(500, 500, 500, 500)), 120.0, 0.01)
  }
}
