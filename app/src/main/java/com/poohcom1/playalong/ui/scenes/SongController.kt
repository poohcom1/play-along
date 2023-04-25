package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poohcom1.playalong.ui.components.VideoPlayer
import com.yausername.youtubedl_android.mapper.VideoInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongController(info: VideoInfo, modifier: Modifier = Modifier) {
    var start: Float by remember { mutableStateOf(0f) }
    var end: Float by remember { mutableStateOf(info.duration.toFloat()) }

    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VideoPlayer(info.url!!)
        RangeSlider(
            value = start..end,
            onValueChange = { start = it.start; end = it.endInclusive },
            valueRange = 0f..info.duration.toFloat(),
            steps = info.duration,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SongController(VideoInfo())
}