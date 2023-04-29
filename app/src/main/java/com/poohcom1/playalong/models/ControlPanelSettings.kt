package com.poohcom1.playalong.models

enum class PopupType {
    NONE, VIDEO_URL
}

data class ControlPanelSettings(
    val playing: Boolean = false,
    val speed: Float = 1.0f,
    val popup: PopupType = PopupType.NONE
)
