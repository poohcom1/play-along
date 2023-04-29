package com.poohcom1.playalong.viewmodels

import com.google.android.exoplayer2.ExoPlayer
import com.yausername.youtubedl_android.mapper.VideoInfo

enum class PopupType {
    NONE, VIDEO_URL
}

data class UiState(
    val playing: Boolean = false,
    val speed: Float = 1.0f,
    val popup: PopupType = PopupType.NONE,
    val loadingVideo: Boolean = false,
    val videoInfo: VideoInfo? = null,
    val player: ExoPlayer? = null,
)
