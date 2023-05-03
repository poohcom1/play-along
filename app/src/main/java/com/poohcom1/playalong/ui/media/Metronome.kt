package com.poohcom1.playalong.ui.media

import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.poohcom1.playalong.R
import com.poohcom1.playalong.datatypes.Tempo
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.roundToLong

@Composable
fun Metronome(videoInfo: VideoInfo?, player: ExoPlayer?, tempo: Tempo, playing: Boolean) {
  val context = LocalContext.current

  val metronomeSoundPool = remember {
    SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
        .build()
  }
  val soundId = remember { metronomeSoundPool.load(context, R.raw.metronome_click, 1) }

  DisposableEffect(metronomeSoundPool) { onDispose { metronomeSoundPool.release() } }

  // FIXME: Not updated when player.currentPosition changes
  LaunchedEffect(tempo, videoInfo, player, playing) {
    if (player == null || videoInfo == null || !playing) return@LaunchedEffect

    val beatTimestamps = mutableListOf<Long>()

    var currentTimestamp = tempo.msOffset

    var beatTimestampIdx = 0

    while (currentTimestamp < videoInfo.duration * 1000) {
      beatTimestamps.add(currentTimestamp.roundToLong())
      currentTimestamp += tempo.msPerBeat

      if (player.currentPosition > beatTimestamps[beatTimestampIdx]) {
        beatTimestampIdx++
      }
    }

    var previousTimestamp = player.currentPosition

    Log.d("Metronome", "Metronome started at beat: $beatTimestampIdx/${beatTimestamps.size}")

    while (beatTimestampIdx < beatTimestamps.size) {
      val beatTimestamp = beatTimestamps[beatTimestampIdx]

      if (beatTimestamp > previousTimestamp && beatTimestamp <= player.currentPosition) {
        withContext(Dispatchers.IO) { metronomeSoundPool.play(soundId, 1f, 1f, 1, 0, 1f) }

        Log.d("Metronome", "Beat: $beatTimestampIdx/${beatTimestamps.size}")
        println("Beat: $beatTimestampIdx/${beatTimestamps.size}")
        beatTimestampIdx++
      }

      previousTimestamp = player.currentPosition

      delay(50)
    }
  }
}
