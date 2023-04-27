package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.ExoPlayer
import com.poohcom1.playalong.ui.components.VideoPlayer
import com.yausername.youtubedl_android.mapper.VideoInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongController(info: VideoInfo, modifier: Modifier = Modifier) {
    var start: Float by remember { mutableStateOf(0f) }
    var end: Float by remember { mutableStateOf(info.duration.toFloat()) }
    var currentPosition by remember { mutableStateOf(0L) }

    val videoURL = info.url

    var loopRange: LongRange by remember { mutableStateOf(0L..info.duration * 1000) }

    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (videoURL != null) {
            VideoPlayer(videoURL, loopRange, 120f, remember { mutableStateOf(currentPosition) })
        }
        RangeSlider(
            value = start..end,
            onValueChange = { range ->
                start = range.start
                end = range.endInclusive
                currentPosition = (range.start * 1000).toLong()
                            },
            valueRange = 0f..info.duration.toFloat(),
            steps = info.duration,
        )

        LaunchedEffect(start, end) {
            loopRange = start.toLong() * 1000..end.toLong() * 1000
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SongController(VideoInfo())
}