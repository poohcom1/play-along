package com.poohcom1.playalong.viewmodels

enum class PopupType {
  NONE,
  VIDEO_URL
}

data class UiState(
    val loading: Boolean = false,
    val playing: Boolean = false,
    val popup: PopupType = PopupType.NONE,
)
