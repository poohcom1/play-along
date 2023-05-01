package com.poohcom1.playalong.viewmodels

import com.google.android.exoplayer2.ExoPlayer
import com.yausername.youtubedl_android.mapper.VideoInfo

data class RootState(
    val speed: Float = 1.0f,
    val loopRangeMs: LongRange = 0L..0L,
    val videoInfo: VideoInfo? = null,
    val player: ExoPlayer? = null,
)