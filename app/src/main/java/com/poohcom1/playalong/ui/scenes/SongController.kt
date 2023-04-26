package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.poohcom1.playalong.models.ControlPanelSettings
import com.poohcom1.playalong.ui.components.VideoPlayer
import com.yausername.youtubedl_android.mapper.VideoInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongController(info: VideoInfo, settings: ControlPanelSettings) {
    var start: Float by remember { mutableStateOf(0f) }
    var end: Float by remember { mutableStateOf(info.duration.toFloat()) }

    var loopRange: LongRange by remember { mutableStateOf(0L..info.duration * 1000) }

    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            VideoPlayer(info.url!!, loopRange, 120f)
            Spacer(Modifier.height(2.dp))
            RangeSlider(
                value = start..end,
                onValueChange = { start = it.start; end = it.endInclusive },
                onValueChangeFinished = { loopRange = start.toLong() * 1000..end.toLong() * 1000 },
                valueRange = 0f..info.duration.toFloat(),
                steps = info.duration,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SongController(VideoInfo(), ControlPanelSettings())
}