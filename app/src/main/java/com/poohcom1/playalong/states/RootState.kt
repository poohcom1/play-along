package com.poohcom1.playalong.states

import com.google.android.exoplayer2.ExoPlayer
import com.poohcom1.playalong.BuildConfig
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.utils.msPerBeatToBpm
import com.yausername.youtubedl_android.mapper.VideoInfo

data class RootState(
    // Activity states
    val loading: Boolean = false,

    // Player states
    val playing: Boolean = false,

    // Controller states
    val speed: Float = 1.0f,
    val loopOn: Boolean = false,
    val loopRangeMs: LongRange = 0L..0L,
    val metronomeOn: Boolean = false,
    val tempo: Tempo =
        Tempo.fromBpm(BuildConfig.DEFAULT_TEMPO).copy(msOffset = BuildConfig.DEFAULT_MS_OFFSET),

    // Video states
    val videoInfo: VideoInfo? = null,
    val player: ExoPlayer? = null,
) {
  val bpm = msPerBeatToBpm(tempo.msPerBeat)
}
