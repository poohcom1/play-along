package com.poohcom1.playalong.utils

import com.poohcom1.playalong.mocks.MockClock
import org.junit.Assert.assertEquals
import org.junit.Test

class TempoTapCalculatorTest {
  @Test
  fun tapTest() {
    val mockClock = MockClock()
    val tempoTap = TempoTapCalculator(clock = mockClock)

    mockClock.currentTime = 500
    tempoTap.tap(0)

    mockClock.currentTime = 1000
    tempoTap.tap(0)

    mockClock.currentTime = 1500
    tempoTap.tap(0)

    assertEquals(500, tempoTap.msPerBeat)
    assertEquals(0, tempoTap.msOffset)
  }

  @Test
  fun msOffsetTest() {
    val mockClock = MockClock()
    val tempoTap = TempoTapCalculator(clock = mockClock)

    mockClock.currentTime = 462 // 130 BPM
    tempoTap.tap(550)

    mockClock.currentTime = 462 * 2
    tempoTap.tap(550 + 462)

    mockClock.currentTime = 462 * 3
    tempoTap.tap(550 + 462 * 2)

    assertEquals(462, tempoTap.msPerBeat)
    assertEquals(374, tempoTap.msOffset)
  }
}
