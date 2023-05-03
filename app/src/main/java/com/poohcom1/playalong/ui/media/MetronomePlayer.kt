package com.poohcom1.playalong.ui.media

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.poohcom1.playalong.R
import com.poohcom1.playalong.datatypes.Tempo
import com.poohcom1.playalong.utils.MetronomeSync
import com.yausername.youtubedl_android.mapper.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

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

  LaunchedEffect(tempo, videoInfo, player, playing) {
    if (player == null || videoInfo == null || !playing) return@LaunchedEffect

    val metronomeSync = MetronomeSync(tempo)

    while (true) {
      if (metronomeSync.tick(player.currentPosition)) {
        withContext(Dispatchers.IO) { metronomeSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f) }
      }

      delay(20)
    }
  }
}
