package com.poohcom1.playalong.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SeekParameters
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView.ControllerVisibilityListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.abs

@Composable
fun VideoPlayer(
    url: String,
    loopRange: LongRange,
    tempo: Float,
    playing: Boolean,
    volume: Float = 0.25f,
    onPlayingSet: (Boolean) -> Unit,
    onPlayerSet: (ExoPlayer?) -> Unit,
    onControllerVisibilitySet: (Boolean) -> Unit,
) {
  val context = LocalContext.current
  val measureDelay = 60f / tempo * 1000

  val exoplayer = remember { ExoPlayer.Builder(context).build() }

  LaunchedEffect(exoplayer) { onPlayerSet(exoplayer) }

  // On url change
  LaunchedEffect(url) {
    exoplayer.setMediaItem(MediaItem.fromUri(url))
    exoplayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
    exoplayer.setSeekParameters(SeekParameters.CLOSEST_SYNC)
    exoplayer.prepare()
  }

  // On loop section change
  LaunchedEffect(loopRange) {
    Log.d("VideoPlayer", "Loop range: $loopRange")

    if (abs(exoplayer.currentPosition - loopRange.first) > 1000) {
      exoplayer.seekTo(loopRange.first + 1)
    }

    withContext(Dispatchers.Main) {
      while (true) {
        if (exoplayer.currentPosition !in loopRange) {
          exoplayer.seekTo(loopRange.first + 1)
          println("Position: ${exoplayer.currentPosition}, Loop range: $loopRange")
        }

        delay(measureDelay.toLong())
      }
    }
  }

  LaunchedEffect(volume) { exoplayer.volume = volume }

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

  Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
    DisposableEffect(
        AndroidView(
            factory = {
              StyledPlayerView(context).apply {
                player = exoplayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                setControllerVisibilityListener(
                    ControllerVisibilityListener { visibility ->
                      onControllerVisibilitySet(visibility == StyledPlayerView.VISIBLE)
                    })
              }
            },
        ),
    ) {
      onDispose {
        exoplayer.release()
        onPlayerSet(null)
        onPlayingSet(false)
      }
    }
  }
}
