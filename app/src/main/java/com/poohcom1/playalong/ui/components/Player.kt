package com.poohcom1.playalong.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poohcom1.playalong.ui.components.ui.theme.PlayAlongTheme
import com.yausername.youtubedl_android.mapper.VideoInfo

@Composable
fun Player(info: VideoInfo, modifier: Modifier = Modifier) {
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = info.title)
    }
}