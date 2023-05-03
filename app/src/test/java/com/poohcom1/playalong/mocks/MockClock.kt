package com.poohcom1.playalong.mocks

import com.poohcom1.playalong.interfaces.Clock

class MockClock : Clock {
  var currentTime = 0L

  override fun currentTimeMs(): Long {
    return currentTime
  }
}
