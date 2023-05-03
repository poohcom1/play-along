package com.poohcom1.playalong.states

import com.google.android.exoplayer2.ExoPlayer
import com.poohcom1.playalong.BuildConfig
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.utils.msPerBeatToBpm
import com.yausername.youtubedl_android.mapper.VideoInfo

data class RootState(
    val speed: Float = 1.0f,
    val loopRangeMs: LongRange = 0L..0L,
    val videoInfo: VideoInfo? = null,
    val player: ExoPlayer? = null,
    val metronomeOn: Boolean = false,
    val tempo: Tempo =
        Tempo.fromBpm(BuildConfig.DEFAULT_TEMPO).copy(msOffset = BuildConfig.DEFAULT_MS_OFFSET),
) {
  val bpm = msPerBeatToBpm(tempo.msPerBeat)
}
