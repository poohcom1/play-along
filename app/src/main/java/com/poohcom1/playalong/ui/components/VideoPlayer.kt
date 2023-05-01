package com.poohcom1.playalong.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    url: String,
    loopRange: LongRange,
    tempo: Float,
    playing: Boolean,
    onPlayingSet: (Boolean) -> Unit,
    onPlayerSet: (ExoPlayer?) -> Unit
) {
  val context = LocalContext.current
  val measureDelay = 60f / tempo * 1000

  val exoplayer = remember { ExoPlayer.Builder(context).build() }

  LaunchedEffect(exoplayer) { onPlayerSet(exoplayer) }

  // On url change
  LaunchedEffect(url) {
    exoplayer.setMediaItem(MediaItem.fromUri(url))
    exoplayer.prepare()
    exoplayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
  }

  // On loop section change
  LaunchedEffect(loopRange) {
    exoplayer.seekTo(loopRange.first)

    withContext(Dispatchers.Main) {
      while (true) {
        if (exoplayer.currentPosition !in loopRange) {
          exoplayer.seekTo(loopRange.first)
        }

        delay(measureDelay.toLong())
      }
    }
  }

  // On play/pause
  LaunchedEffect(playing) {
    exoplayer.addListener(
        object : Player.Listener {
          override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            onPlayingSet(playWhenReady)
          }
        })

    if (playing) {
      exoplayer.play()
    } else {
      exoplayer.pause()
    }
  }

  // Render

  DisposableEffect(
      AndroidView(
          factory = {
            StyledPlayerView(context).apply {
              player = exoplayer
              resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
              setShowNextButton(false)
              setShowPreviousButton(false)
              // FIXME: Controller is getting clipped by the 16dp padding
            }
          },
          modifier = modifier.height(200.dp)),
  ) {
    onDispose {
      exoplayer.release()
      onPlayerSet(null)
      onPlayingSet(false)
    }
  }
}
