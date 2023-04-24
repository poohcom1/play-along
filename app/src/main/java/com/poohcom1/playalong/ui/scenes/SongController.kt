package com.poohcom1.playalong.ui.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poohcom1.playalong.ui.components.VideoPlayer
import com.yausername.youtubedl_android.mapper.VideoInfo

@Composable
fun SongController(info: VideoInfo, modifier: Modifier = Modifier) {
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VideoPlayer(info.url)
    }
}