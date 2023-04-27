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
fun PlayerController(
    info: VideoInfo,
    settings: ControlPanelSettings,
    onSettingsChange: (ControlPanelSettings) -> Unit
) {
    var loopRange: LongRange by remember { mutableStateOf(0L..info.duration * 1000) }
    var isSeeking by remember { mutableStateOf(false) }
    var wasPlayingBeforeSeek by remember { mutableStateOf(false) }

    Column(
        Modifier.padding(horizontal = 128.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            VideoPlayer(info.url!!, loopRange, 120f, playing = settings.playing, onPlayingSet = {
                onSettingsChange(settings.copy(playing = it))
            })
            Spacer(Modifier.height(16.dp))
            RangeSlider(
                value = (loopRange.first / 1000).toFloat()..(loopRange.last / 1000).toFloat(),
                onValueChange = {
                    if (!isSeeking) {
                        isSeeking = true
                        if (settings.playing) {
                            wasPlayingBeforeSeek = true
                            onSettingsChange(settings.copy(playing = false))
                        }
                    }
                    loopRange = it.start.toLong() * 1000..it.endInclusive.toLong() * 1000
                },
                valueRange = 0f..info.duration.toFloat(),
                onValueChangeFinished = {
                    isSeeking = false
                    if (wasPlayingBeforeSeek) {
                        wasPlayingBeforeSeek = false
                        onSettingsChange(settings.copy(playing = true))
                    }
                },
                steps = info.duration,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PlayerController(VideoInfo(), ControlPanelSettings(), onSettingsChange = { })
}