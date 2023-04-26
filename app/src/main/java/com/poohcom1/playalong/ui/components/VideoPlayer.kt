package com.poohcom1.playalong.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun VideoPlayer(url: String, range: LongRange, tempo: Float) {
    val context = LocalContext.current

    val measureDelay = 60f / tempo * 1000

    val exoplayer = remember {
        ExoPlayer.Builder(context).build()
    }

    exoplayer.stop()
    exoplayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
    exoplayer.seekTo(range.first)

    LaunchedEffect(range) {
        exoplayer.setMediaItem(MediaItem.fromUri(url))
        exoplayer.prepare()

        withContext(Dispatchers.Main) {
            while (true) {
                if (exoplayer.currentPosition !in range) {
                    exoplayer.seekTo(range.first)
                    Log.d("VideoPlayer", "Seeking to ${range.first}...")
                }

                delay(measureDelay.toLong())
            }
        }
    }

    DisposableEffect(
        AndroidView(
            factory = {
                StyledPlayerView(context).apply {
                    player = exoplayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            }
        )
    ) {
        onDispose { exoplayer.release() }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun Preview() {
//    PlayAlongTheme {
//        VideoPlayer("https://www.youtube.com/watch?v=te6JsCBrZn0")
//    }
//}