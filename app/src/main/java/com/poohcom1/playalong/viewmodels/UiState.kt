package com.poohcom1.playalong.viewmodels

data class UiState(
    val loading: Boolean = false,
    val playing: Boolean = false,
    val popup: PopupType = PopupType.NONE,
) {
  enum class PopupType {
    NONE,
    VIDEO_URL,
    TEMPO_PICKER,
  }
}
